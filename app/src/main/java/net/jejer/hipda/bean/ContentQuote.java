package net.jejer.hipda.bean;

import android.text.TextUtils;

import net.jejer.hipda.utils.HtmlCompat;
import net.jejer.hipda.utils.Utils;

public class ContentQuote extends ContentAbs {
    private final String mQuote;
    private final String postId;
    private String author;
    private String to;
    private String time;
    private String text;

    public ContentQuote(String postText, String authorAndTime, String postId) {
        this.postId = postId;
        mQuote = Utils.nullToText(postText) + Utils.nullToText(authorAndTime);
        //replace chinese space and trim
        text = HtmlCompat.fromHtml(postText).toString().replace("　", " ").replace(String.valueOf((char) 160), " ").trim();
        if (!TextUtils.isEmpty(authorAndTime) && authorAndTime.contains("发表于")) {
            author = authorAndTime.substring(0, authorAndTime.indexOf("发表于")).trim();
            time = authorAndTime.substring(authorAndTime.indexOf("发表于") + "发表于".length()).trim();
        }
        if (text.startsWith("回复")) {
            text = text.substring("回复".length()).trim();
            //this is not accurate, will use postId if available
            int idx = text.indexOf("    ");
            if (idx > 0 && idx < 10) {
                to = text.substring(0, idx).trim();
            } else if (text.indexOf(" ") > 0) {
                to = text.substring(0, text.indexOf(" ")).trim();
            }
            if (!TextUtils.isEmpty(to))
                text = text.substring(to.length() + 1).trim();
        }
    }

    @Override
    public String getContent() {
        return mQuote;
    }

    @Override
    public String getCopyText() {
        return "『" + Utils.fromHtmlAndStrip(mQuote) + "』";
    }

    public String getAuthor() {
        return author;
    }

    public String getTime() {
        return time;
    }

    public String getTo() {
        return to;
    }

    public String getText() {
        return text;
    }

    public String getPostId() {
        return postId;
    }

    public boolean isReplyQuote() {
        return !TextUtils.isEmpty(author) && !TextUtils.isEmpty(text);
    }
}
