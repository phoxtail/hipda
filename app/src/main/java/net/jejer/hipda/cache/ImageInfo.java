package net.jejer.hipda.cache;

import net.jejer.hipda.R;
import net.jejer.hipda.ui.HiApplication;
import net.jejer.hipda.utils.Utils;

/**
 * store loaded image's size
 * Created by GreenSkinMonster on 2015-04-24.
 */
public class ImageInfo {

    public static final int IDLE = 0;
    public static final int IN_PROGRESS = 1;
    public static final int FAIL = 2;
    public static final int SUCCESS = 3;
    private final static int MAX_WIDTH = Math.min(getMaxBitmapWidth(), (int) (Utils.getScreenWidth() * 0.8));
    private final static int MAX_HEIGHT = Utils.getScreenHeight();
    private final static int MAX_VIEW_WIDTH = Utils.getScreenWidth()
            - 2 * (int) HiApplication.getAppContext().getResources().getDimension(R.dimen.thread_detail_padding);
    private final static int MAX_VIEW_HEIGHT = (int) (Utils.getScreenHeight() * 1.2);
    private String mUrl;
    private int mWidth;
    private int mHeight;
    private String mPath;
    private String mMime;
    private long mFileSize;
    private int mOrientation;
    private int mProgress;
    private int mStatus = IDLE;
    private String mMessage;

    public ImageInfo(String url) {
        mUrl = url;
    }

    private static int getMaxBitmapWidth() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        if (maxMemory <= 128 * 1024 * 1024) {
            return 560;
        } else if (maxMemory <= 256 * 1024 * 1024) {
            return 720;
        }
        return 800;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public String getMime() {
        return mMime;
    }

    public void setMime(String mime) {
        mMime = mime;
    }

    public long getFileSize() {
        return mFileSize;
    }

    public void setFileSize(long fileSize) {
        mFileSize = fileSize;
    }

    public boolean isSuccess() {
        return mStatus == SUCCESS;
    }

    public boolean isInProgress() {
        return mStatus == IN_PROGRESS;
    }

    public boolean isFail() {
        return mStatus == FAIL;
    }

    public boolean isIdle() {
        return mStatus == IDLE;
    }

    public boolean isGif() {
        return mMime != null && mMime.contains("gif");
    }

    public boolean isLongImage() {
        return !isGif() && mHeight >= 2.5 * mWidth;
    }

    public int getOrientation() {
        return mOrientation;
    }

    public void setOrientation(int orientation) {
        mOrientation = orientation;
    }

    public int getBitmapHeight() {
        return Math.round(getHeight() * getMaxBitmapScaleRate());
    }

    public int getBitmapWidth() {
        return Math.round(getWidth() * getMaxBitmapScaleRate());
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        mProgress = progress;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        if (mStatus == SUCCESS)
            return;
        mStatus = status;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getUrl() {
        return mUrl;
    }

    public int getViewHeight() {
        int viewWidth = (int) Math.pow(mWidth, 1.3);
        if (isGif() || viewWidth > MAX_VIEW_WIDTH / 2)
            viewWidth = MAX_VIEW_WIDTH;

        int viewHeight = Math.round(viewWidth * 1.0f * mHeight / mWidth);

        //at last, limit ImageView max height
        if (viewHeight > MAX_VIEW_HEIGHT) {
            viewHeight = MAX_VIEW_HEIGHT;
        }

        return viewHeight;
    }

    private float getMaxBitmapScaleRate() {
        float scaleW = (float) MAX_WIDTH / getWidth();
        float scaleH = (float) MAX_HEIGHT / getHeight();
        float scale = (float) Math.round(Math.min(scaleH, scaleW) * 10) / 10;
        if (scale > 1)
            scale = 1;
        if (scale < 0.1)
            scale = 0.1f;
        return scale;
    }

}
