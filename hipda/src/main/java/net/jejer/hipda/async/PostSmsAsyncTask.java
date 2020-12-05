package net.jejer.hipda.async;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;

import net.jejer.hipda.okhttp.ParamsMap;
import net.jejer.hipda.utils.Constants;
import net.jejer.hipda.utils.HiUtils;
import net.jejer.hipda.utils.Logger;
import net.jejer.hipda.utils.UIUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import static net.jejer.hipda.okhttp.OkHttpHelper.getErrorMessage;
import static net.jejer.hipda.okhttp.OkHttpHelper.getInstance;

public class PostSmsAsyncTask extends AsyncTask<String, Void, Void> {

    private static final long SMS_DELAY_IN_SECS = 15;
    private static long LAST_SMS_TIME = 0;
    private Context mCtx;
    private String mUid;
    private String mUsername;

    private String mFormhash;
    private int mStatus = Constants.STATUS_FAIL;
    private String mResult = "";
    private SmsPostListener mPostListenerCallback;
    private AlertDialog mDialog;

    public PostSmsAsyncTask(Context ctx, String uid, String username, SmsPostListener postListener, AlertDialog dialog) {
        mCtx = ctx;
        mUid = uid;
        mUsername = username;
        mPostListenerCallback = postListener;
        mDialog = dialog;
    }

    public static int getWaitTimeToSendSms() {
        long delta = (System.currentTimeMillis() - LAST_SMS_TIME) / 1000;
        if (SMS_DELAY_IN_SECS > delta) {
            return (int) (SMS_DELAY_IN_SECS - delta);
        }
        return 0;
    }

    @Override
    protected Void doInBackground(String... arg0) {
        String content = arg0[0];

        // fetch a new page and parse formhash
        String rsp_str = "";
        Boolean done = false;
        int retry = 0;
        do {
            try {
                rsp_str = getInstance().get((HiUtils.SMSPreparePostUrl + mUid));
                if (!TextUtils.isEmpty(rsp_str)) {
                    if (!LoginHelper.checkLoggedin(mCtx, rsp_str)) {
                        int status = new LoginHelper(mCtx).login();
                        if (status == Constants.STATUS_FAIL_ABORT) {
                            break;
                        }
                    } else {
                        done = true;
                    }
                }
            } catch (Exception e) {
                mResult = getErrorMessage(e).getMessage();
            }
            retry++;
        } while (!done && retry < 3);

        if (done) {
            Document doc = Jsoup.parse(rsp_str);
            Elements formhashES = doc.select("input#formhash");
            if (formhashES.size() == 0) {
                mResult = "SMS send fail, can not get formhash.";
                return null;
            } else {
                mFormhash = formhashES.first().attr("value");
            }
            // do post
            doPost(content);
        }

        return null;
    }

    private String doPost(String content) {

        String url;
        if (!TextUtils.isEmpty(mUid))
            url = HiUtils.SMSPostByUid.replace("{uid}", mUid);
        else
            url = HiUtils.SMSPostByUsername.replace("{username}", mUsername);

        ParamsMap params = new ParamsMap();
        params.put("formhash", mFormhash);
        params.put("lastdaterange", String.valueOf(System.currentTimeMillis()));
        params.put("handlekey", "pmreply");
        params.put("message", content);
        if (TextUtils.isEmpty(mUid))
            params.put("msgto", mUsername);

        String response = null;
        try {
            response = getInstance().post(url, params);

            //response is in xml format
            if (TextUtils.isEmpty(response)) {
                mResult = "短消息发送失败 :  无返回结果";
            } else if (!response.contains("class=\"summary\"")) {
                String result = "";
                Document doc = Jsoup.parse(response, "", Parser.xmlParser());
                for (Element e : doc.select("root")) {
                    result = e.text();
                    if (result.contains("<"))
                        result = result.substring(0, result.indexOf("<"));
                }
                if (!TextUtils.isEmpty(result)) {
                    mResult = result;
                } else {
                    mResult = "短消息发送失败.";
                }
            } else {
                mResult = "短消息发送成功.";
                mStatus = Constants.STATUS_SUCCESS;
                LAST_SMS_TIME = System.currentTimeMillis();
            }
        } catch (Exception e) {
            Logger.e(e);
            mResult = "短消息发送失败 :  " + getErrorMessage(e);
        }
        return response;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mPostListenerCallback != null) {
            mPostListenerCallback.onSmsPrePost();
        } else {
            UIUtils.toast("正在发送...");
        }
    }

    @Override
    protected void onPostExecute(Void avoid) {
        super.onPostExecute(avoid);
        if (mPostListenerCallback != null) {
            mPostListenerCallback.onSmsPostDone(mStatus, mResult, mDialog);
        } else {
            UIUtils.toast(mResult);
        }
    }

    public interface SmsPostListener {
        void onSmsPrePost();

        void onSmsPostDone(int status, String message, AlertDialog dialog);
    }

}
