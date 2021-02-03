package net.jejer.hipda.glide;

/**
 * Image loading event
 * Created by GreenSkinMonster on 2015-08-27.
 */
public class GlideImageEvent {

    private final String mUrl;
    private final int mProgress;
    private final int mStatus;

    public GlideImageEvent(String url, int progress, int status) {
        mUrl = url;
        mStatus = status;
        mProgress = progress;
    }

    public String getImageUrl() {
        return mUrl;
    }

    public int getProgress() {
        return mProgress;
    }

    public int getStatus() {
        return mStatus;
    }
}
