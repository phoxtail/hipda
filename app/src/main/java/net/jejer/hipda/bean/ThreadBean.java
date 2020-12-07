package net.jejer.hipda.bean;


@SuppressWarnings("SpellCheckingInspection")
public class ThreadBean {

    private String mTitle;
    private String mTitleColor;
    private String mTid;

    private String mAuthor;
    private String mAuthorId;
    private String mAvatarUrl;

    private String mCountCmts;
    private String mCountViews;

    private String mTimeCreate;
    private Boolean mHavePic;
    private String mType;
    private int mMaxPage;

    public ThreadBean() {
        mHavePic = false;
    }

    public String getTitle() {
        return mTitle;
    }


    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getTitleColor() {
        return mTitleColor;
    }

    public void setTitleColor(String mTitleColor) {
        this.mTitleColor = mTitleColor;
    }

    public String getTid() {
        return mTid;
    }


    public void setTid(String mTid) {
        this.mTid = mTid;
    }


    public String getAuthor() {
        return mAuthor;
    }


    // return false if author is in blacklist
    public boolean setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;

        return HiSettingsHelper.getInstance().notInBlocklist(mAuthor);
    }


    public String getAuthorId() {
        return mAuthorId;
    }


    public void setAuthorId(String mAuthorId) {
        this.mAuthorId = mAuthorId;
    }

    public String getCountCmts() {
        return mCountCmts;
    }


    public void setCountCmts(String mCountCmts) {
        this.mCountCmts = mCountCmts;
    }


    public String getCountViews() {
        return mCountViews;
    }


    public void setCountViews(String mCountViews) {
        this.mCountViews = mCountViews;
    }


    public String getTimeCreate() {
        return mTimeCreate;
    }


    public void setTimeCreate(String mTimeCreate) {
        this.mTimeCreate = mTimeCreate;
    }


    public Boolean getHavePic() {
        return mHavePic;
    }

    public void setHavePic(Boolean mHavePic) {
        this.mHavePic = mHavePic;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        if (avatarUrl.contains("noavatar")) {
            this.mAvatarUrl = "";
        } else {
            this.mAvatarUrl = avatarUrl;
        }
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }


    public int getMaxPage() {
        return mMaxPage;
    }

    public void setMaxPage(int lastPage) {
        this.mMaxPage = lastPage;
    }
}
