package net.jejer.hipda.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.bumptech.glide.Glide;

import net.jejer.hipda.bean.HiSettingsHelper;
import net.jejer.hipda.ui.HiApplication;
import net.jejer.hipda.utils.Connectivity;
import net.jejer.hipda.utils.HiUtils;
import net.jejer.hipda.utils.Logger;
import net.jejer.hipda.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * helper class for okhttp
 * Created by GreenSkinMonster on 2015-10-22.
 */
public class OkHttpHelper {

    public final static int NETWORK_TIMEOUT_SECS = 10;
    public final static int MAX_RETRY_TIMES = 3;

    public final static int FORCE_NETWORK = 1;
    public final static int FORCE_CACHE = 2;
    public final static int PREFER_CACHE = 3;

    public final static String CACHE_DIR_NAME = "okhttp";

    public static final String ERROR_CODE_PREFIX = "Unexpected code ";
    private final static CacheControl PREFER_CACHE_CTL = new CacheControl.Builder()
            .maxStale(3 * 60, TimeUnit.SECONDS)
            .build();
    private final ResultCallback DEFAULT_CALLBACK = new ResultCallback() {
        @Override
        public void onError(Exception e) {
        }

        @Override
        public void onResponse(String response) {
        }
    };
    private final OkHttpClient mClient;
    private final PersistentCookieStore mCookieStore;
    private final Handler handler;

    private OkHttpHelper() {
        mCookieStore = new PersistentCookieStore(HiApplication.getAppContext(), HiUtils.CookieDomain);
        CookieJar mCookieJar = new CookieJar() {
            @Override
            public void saveFromResponse(@NotNull HttpUrl url, @NotNull List<Cookie> cookies) {
                if (cookies != null && cookies.size() > 0) {
                    for (Cookie item : cookies) {
                        mCookieStore.add(url, item);
                    }
                }
            }

            @NotNull
            @Override
            public List<Cookie> loadForRequest(@NotNull HttpUrl url) {
                return mCookieStore.get(url);
            }
        };
        Cache cache = new Cache(Glide.getPhotoCacheDir(HiApplication.getAppContext(), CACHE_DIR_NAME), 10 * 1024 * 1024);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(OkHttpHelper.NETWORK_TIMEOUT_SECS, TimeUnit.SECONDS)
                .readTimeout(OkHttpHelper.NETWORK_TIMEOUT_SECS, TimeUnit.SECONDS)
                .writeTimeout(OkHttpHelper.NETWORK_TIMEOUT_SECS, TimeUnit.SECONDS)
                .cache(cache)
                .cookieJar(mCookieJar);

        if (HiSettingsHelper.getInstance().isTrustAllCerts()) {
            setupTrustAllCerts(builder);
        }

        if (Logger.isDebug())
            builder.addInterceptor(new LoggingInterceptor());

        mClient = builder.build();
        handler = new Handler(Looper.getMainLooper());
    }

    public static void setupTrustAllCerts(OkHttpClient.Builder builder) {
        try {
            X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            };
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            builder.sslSocketFactory(sslContext.getSocketFactory(),
                    trustManager);
            builder.hostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static OkHttpHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static String getResponseBody(Response response) throws IOException {
        if (!response.isSuccessful()) {
            throw new IOException(ERROR_CODE_PREFIX + response.code() + ", " + response.message());
        }

        String encoding = HiSettingsHelper.getInstance().getEncode();
        String contextType = response.headers().get("Content-Type");
        if (!TextUtils.isEmpty(contextType)) {
            if (contextType.toUpperCase().contains("UTF")) {
                encoding = "UTF-8";
            } else if (contextType.toUpperCase().contains("GBK")) {
                encoding = "GBK";
            }
        }
        return new String(response.body().bytes(), encoding);
    }

    public static NetworkError getErrorMessage(Exception e) {
        int errCode = 0;
        String msg = e.getClass().getSimpleName();
        if (HiApplication.getAppContext() != null
                && !Connectivity.isConnected(HiApplication.getAppContext())) {
            msg = "请检查网络连接";
        } else if (e instanceof UnknownHostException) {
            msg = "请检查网络连接.";
        } else if (e instanceof SocketTimeoutException) {
            msg = "请求超时";
        } else if (e instanceof IOException) {
            String error = e.getMessage();
            if (error != null && error.contains(ERROR_CODE_PREFIX)) {
                errCode = Utils.parseInt(Utils.getMiddleString(error, ERROR_CODE_PREFIX, ",").trim());
                if (errCode > 0)
                    msg = "错误代码 (" + errCode + ")";
            }
        }
        return new NetworkError(errCode, msg, e.getClass().getName() + "\n" + e.getMessage());
    }

    public OkHttpClient getClient() {
        return mClient;
    }

    private Request buildGetRequest(String url, Object tag, CacheControl cacheControl) {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .header("User-Agent", HiUtils.getUserAgent());

        if (cacheControl != null)
            builder.cacheControl(cacheControl);

        if (tag != null)
            builder.tag(tag);

        return builder.build();
    }

    private Request buildPostFormRequest(String url, ParamsMap params)
            throws UnsupportedEncodingException {

        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, List<String>> entry : params.entrySet()) {
                for (String value : entry.getValue()) {
                    builder.addEncoded(entry.getKey(),
                            URLEncoder.encode(value, HiSettingsHelper.getInstance().getEncode()));
                }
            }
        }

        RequestBody requestBody = builder.build();
        Request.Builder reqBuilder = new Request.Builder();
        reqBuilder.url(url)
                .header("User-Agent", HiUtils.getUserAgent())
                .post(requestBody)
                .cacheControl(CacheControl.FORCE_NETWORK);

        if (null != null)
            reqBuilder.tag(null);

        return reqBuilder.build();
    }

    public String get(String url) throws IOException {
        return get(url, null);
    }

    public Response getAsResponse(String url) throws IOException {
        Request request = buildGetRequest(url, null, CacheControl.FORCE_NETWORK);

        Call call = mClient.newCall(request);
        return call.execute();
    }

    public String get(String url, String tag) throws IOException {
        return get(url, tag, FORCE_NETWORK);
    }

    public String get(String url, String tag, int cacheType) throws IOException {
        Request request = buildGetRequest(url, tag, getCacheControl(cacheType));

        Call call = mClient.newCall(request);
        Response response = call.execute();

        return getResponseBody(response);
    }

    public void asyncGet(String url, ResultCallback callback) {
        asyncGet(url, FORCE_NETWORK, callback, null);
    }

    public void asyncGet(String url, int cacheType, ResultCallback callback, Object tag) {
        if (callback == null) callback = DEFAULT_CALLBACK;
        final ResultCallback rspCallBack = callback;

        Request request = buildGetRequest(url, tag, getCacheControl(cacheType));
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handleFailureCallback(e, rspCallBack);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String body = getResponseBody(response);
                    handleSuccessCallback(body, rspCallBack);
                } catch (IOException e) {
                    handleFailureCallback(e, rspCallBack);
                }
            }
        });
    }

    public String post(String url, ParamsMap params) throws IOException {
        Request request = buildPostFormRequest(url, params);
        Response response = mClient.newCall(request).execute();
        return getResponseBody(response);
    }

    public Response postAsResponse(String url, ParamsMap params) throws IOException {
        Request request = buildPostFormRequest(url, params);
        return mClient.newCall(request).execute();
    }

    public void asyncPost(String url, ParamsMap params, ResultCallback callback)
            throws UnsupportedEncodingException {
        if (callback == null) callback = DEFAULT_CALLBACK;
        final ResultCallback rspCallBack = callback;

        Request request = buildPostFormRequest(url, params);
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handleFailureCallback(e, rspCallBack);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String body = getResponseBody(response);
                    handleSuccessCallback(body, rspCallBack);
                } catch (IOException e) {
                    handleFailureCallback(e, rspCallBack);
                }
            }
        });
    }

    private void handleFailureCallback(final Exception e, final ResultCallback callback) {
        handler.post(() -> {
            Logger.e(e.getClass().getName() + "\n" + e.getMessage());
            callback.onError(e);
        });
    }

    private void handleSuccessCallback(final String response, final ResultCallback callback) {
        handler.post(() -> callback.onResponse(response));
    }

    public void clearCookies() {
        if (mCookieStore != null)
            mCookieStore.removeAll();
    }

    public boolean isLoggedIn() {
        List<Cookie> cookies = mCookieStore.getCookies();
        for (Cookie cookie : cookies) {
            if ("cdb_auth".equals(cookie.name())) {
                return true;
            }
        }
        return false;
    }

    public String getAuthCookie() {
        List<Cookie> cookies = mCookieStore.getCookies();
        for (Cookie cookie : cookies) {
            if ("cdb_auth".equals(cookie.name())) {
                return cookie.value();
            }
        }
        return null;
    }

    private CacheControl getCacheControl(int cacheType) {
        if (cacheType == FORCE_NETWORK) {
            return CacheControl.FORCE_NETWORK;
        } else if (cacheType == FORCE_CACHE) {
            return CacheControl.FORCE_CACHE;
        } else if (cacheType == PREFER_CACHE) {
            return PREFER_CACHE_CTL;
        }
        return null;
    }

    public interface ResultCallback {

        void onError(Exception e);

        void onResponse(String response);
    }

    private static class SingletonHolder {
        static final OkHttpHelper INSTANCE = new OkHttpHelper();
    }
}
