package net.jejer.hipda.db;

import net.jejer.hipda.utils.Utils;

import java.util.Date;

/**
 * Created by GreenSkinMonster on 2016-07-23.
 */
public class Content {

    private final long mTime;
    private final String mContent;

    public Content(String content, long time) {
        mContent = content;
        mTime = time;
    }

    public String getContent() {
        return mContent;
    }

    public String getDesc() {
        return "输入于 "
                + Utils.shortyTime(new Date(mTime))
                + "，共 " + Utils.getWordCount(mContent) + " 字";
    }
}
