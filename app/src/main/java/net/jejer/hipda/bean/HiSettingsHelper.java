package net.jejer.hipda.bean;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.text.TextUtils;

import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import net.jejer.hipda.R;
import net.jejer.hipda.service.NotiHelper;
import net.jejer.hipda.ui.HiApplication;
import net.jejer.hipda.utils.Connectivity;
import net.jejer.hipda.utils.Constants;
import net.jejer.hipda.utils.HiUtils;
import net.jejer.hipda.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("SpellCheckingInspection")
public class HiSettingsHelper {
    /*
     *
     * NOTE! PLEASE LINE-UP WITH PREFERENCE.XML
     *
     * */
    public static final String PERF_USERNAME = "PERF_USERNAME";
    public static final String PERF_PASSWORD = "PERF_PASSWORD";
    public static final String PERF_UID = "PERF_UID";
    public static final String PERF_SECQUESTION = "PERF_SECQUESTION";
    public static final String PERF_SECANSWER = "PERF_SECANSWER";
    public static final String PERF_SHOWSTICKTHREADS = "PERF_SHOWSTICKTHREADS";
    public static final String PERF_WIFI_IMAGE_POLICY = "PERF_WIFI_IMAGE_POLICY";
    public static final String PERF_MOBILE_IMAGE_POLICY = "PERF_MOBILE_IMAGE_POLICY";
    public static final String PERF_AVATAR_LOAD_TYPE = "PERF_AVATAR_LOAD_TYPE";
    public static final String PERF_SORTBYPOSTTIME_BY_FORUM = "PERF_SORTBYPOSTTIME_BY_FORUM";
    public static final String PERF_ADDTAIL = "PERF_ADDTAIL";
    public static final String PERF_TAILTEXT = "PERF_TAILTEXT";
    public static final String PERF_TAILURL = "PERF_TAILURL";
    public static final String PERF_THEME = "PERF_THEME";
    public static final String PERF_PRIMARY_COLOR = "PERF_PRIMARY_COLOR";
    public static final String PERF_NIGHT_THEME = "PERF_NIGHT_THEME";
    public static final String PERF_NIGHT_MODE = "PERF_NIGHT_MODE";
    public static final String PERF_NAVBAR_COLORED = "PERF_NAVBAR_COLORED";
    public static final String PERF_FONT = "PERF_FONT";
    public static final String PERF_FORUMS = "PERF_FORUMS2";
    public static final String PERF_FREQ_MENUS = "PERF_FREQ_MENUS";
    public static final String PERF_ENCODEUTF8 = "PERF_ENCODEUTF8";
    public static final String PERF_OLD_BLACKLIST = "PERF_BLANKLIST_USERNAMES";
    public static final String PERF_TEXTSIZE_POST_ADJ = "PERF_TEXTSIZE_POST_ADJ";
    public static final String PERF_TEXTSIZE_TITLE_ADJ = "PERF_TEXTSIZE_TITLE_ADJ";
    public static final String PERF_SCREEN_ORIENTATION = "PERF_SCREEN_ORIENTATION";
    public static final String PERF_GESTURE_BACK = "PERF_GESTURE_BACK";
    public static final String PERF_APP_BAR_COLLAPSIBLE = "PERF_APP_BAR_COLLAPSIBLE";
    public static final String PERF_FAB_LEFT_SIDE = "PERF_FAB_LEFT_SIDE";
    public static final String PERF_FAB_AUTO_HIDE = "PERF_FAB_AUTO_HIDE";
    public static final String PERF_CLICK_EFFECT = "PERF_CLICK_EFFECT";
    public static final String PERF_LAST_UPDATE_CHECK = "PERF_LAST_UPDATE_CHECK";
    public static final String PERF_AUTO_UPDATE_CHECK = "PERF_AUTO_UPDATE_CHECK";
    public static final String PERF_ABOUT = "PERF_ABOUT";
    public static final String PERF_SUPPORT = "PERF_SUPPORT";
    public static final String PERF_MAX_POSTS_IN_PAGE = "PERF_MAX_POSTS_IN_PAGE";
    public static final String PERF_POST_LINE_SPACING = "PERF_POST_LINE_SPACING";
    public static final String PERF_LAST_FORUM_ID = "PERF_LAST_FORUM_ID";
    public static final String PERF_ERROR_REPORT_MODE = "PERF_ERROR_REPORT_MODE";
    public static final String PERF_INSTALLED_VERSION = "PERF_INSTALLED_VERSION";
    public static final String PERF_CLEAR_CACHE = "PERF_CLEAR_CACHE";
    public static final String PERF_CLEAR_IMAGE_CACHE = "PERF_CLEAR_IMAGE_CACHE";
    public static final String PERF_NOTI_TASK_ENABLED = "PERF_NOTI_TASK_ENABLED";
    public static final String PERF_NOTI_JOB_LAST_TIME = "PERF_NOTI_JOB_LAST_TIME";
    public static final String PERF_NOTI_SOUND = "PERF_NOTI_SOUND";
    public static final String PERF_NOTI_SILENT_MODE = "PERF_NOTI_SILENT_MODE";
    public static final String PERF_NOTI_SILENT_BEGIN = "PERF_NOTI_SILENT_BEGIN";
    public static final String PERF_NOTI_SILENT_END = "PERF_NOTI_SILENT_END";
    public static final String PERF_BS_TYPE_ID = "PERF_BS_TYPE_ID";
    public static final String PERF_SAVE_FOLDER = "PERF_SAVE_FOLDER";
    public static final String PERF_CIRCLE_AVATAR = "PERF_CIRCLE_AVATAR";
    public static final String PERF_LAST_TASK_TIME = "PERF_LAST_TASK_TIME";
    public static final String PERF_CACHE_SIZE_IN_MB = "PERF_CACHE_SIZE_IN_MB";
    public static final String PERF_FORUM_SERVER = "PERF_FORUM_SERVER";
    public static final String PERF_IMAGE_HOST = "PERF_IMAGE_HOST";
    public static final String PERF_IMAGE_HOST_UPDATE_TIME = "PERF_IMAGE_HOST_UPDATE_TIME";
    public static final String PERF_TRUST_ALL_CERTS = "PERF_TRUST_ALL_CERTS";
    public static final String PERF_MAX_UPLOAD_FILE_SIZE = "PERF_MAX_UPLOAD_FILE_SIZE";
    public static final String PERF_SHOW_TAIL = "PERF_SHOW_TAIL";
    public static final String PERF_CAMERA_PERM_ASKED = "PERF_CAMERA_PERM_ASKED";
    public static final String PERF_SWIPE_COMPAT_MODE = "PERF_SWIPE_COMPAT_MODE";
    public static final String PERF_BLACKLIST = "PERF_BLACKLIST";
    public static final String PERF_BLACKLIST_SYNC_TIME = "PERF_BLACKLIST_SYNC_TIME";

    public static final String THEME_LIGHT = "light";
    public static final String THEME_DARK = "dark";
    public static final String THEME_BLACK = "black";
    public static final int MAX_TAIL_TEXT_LENGTH = 12;

    public static final int SMALL_IMAGE_SIZE = 500 * 1024; //500K
    public static final String IMAGE_POLICY_NONE = "none";
    public static final String IMAGE_POLICY_SMALL = "small_images";
    public static final String IMAGE_POLICY_THUMB = "prefer_thumb";
    public static final String IMAGE_POLICY_ORIGINAL = "prefer_original";
    private static boolean mMobileNetwork;
    private final Context mCtx;
    private final SharedPreferences mSharedPref;
    private String mUsername = "";
    private String mPassword = "";
    private String mSecQuestion = "";
    private String mSecAnswer = "";
    private String mUid = "";
    private boolean mShowStickThreads = false;
    private String mAvatarLoadType = "0";
    private Set<String> mSortByPostTimeByForum;
    private boolean mAddTail = true;
    private String mTailText = "";
    private String mTailUrl = "";
    private String mTheme = "";
    private int mPrimaryColor = 0;
    private String mNightTheme = "";
    private boolean mNightMode = false;
    private boolean mNavBarColor = false;
    private String mFont = "";
    private List<Integer> mForums = new ArrayList<>();
    private Set<String> mFreqMenus = new HashSet<>();
    private boolean mEncodeUtf8 = false;
    private List<String> mOldBlocklists;
    private List<String> mBlocklists;
    private int mPostTextSizeAdj = 0;
    private int mPostLineSpacing = 0;
    private int mTitleTextSizeAdj = 0;
    private int mScreenOrientation = ActivityInfo.SCREEN_ORIENTATION_USER;
    private boolean mGestureBack = true;
    private int mMaxPostsInPage;
    private int mLastForumId = 0;
    private boolean mErrorReportMode;
    private boolean mNotiTaskEnabled;
    private String mBSTypeId;
    private String mForumServer;
    private String mImageHost;
    // --------------- THIS IS NOT IN PERF -----------
    private int mBasePostTextSize = -1;
    private int mBaseTitleTextSize = -1;
    private long mLastCheckSmsTime;

    private HiSettingsHelper() {
        mCtx = HiApplication.getAppContext();
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(mCtx);
        reload();
    }

    public static boolean isMobileNetwork() {
        return mMobileNetwork;
    }

    public static void setMobileNetwork(boolean mobileNetwork) {
        mMobileNetwork = mobileNetwork;
    }

    public static void updateMobileNetworkStatus(Context context) {
        if (context != null && Connectivity.isConnected(context))
            setMobileNetwork(!Connectivity.isConnectedWifi(context));
    }

    @SuppressWarnings("SameReturnValue")
    public static HiSettingsHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public boolean isImageLoadable(long imageSize, boolean isThumb) {
        String policy = getCurrectImagePolicy();
        return IMAGE_POLICY_ORIGINAL.equals(policy)
                || (IMAGE_POLICY_THUMB.equals(policy))
                || (IMAGE_POLICY_SMALL.equals(policy) && (isThumb || (imageSize > 0 && imageSize <= SMALL_IMAGE_SIZE)));
    }

    public String getCurrectImagePolicy() {
        if (mMobileNetwork) {
            return getStringValue(PERF_MOBILE_IMAGE_POLICY, IMAGE_POLICY_NONE);
        } else {
            return getStringValue(PERF_WIFI_IMAGE_POLICY, IMAGE_POLICY_THUMB);
        }
    }

    public boolean isLoadAvatar() {
        return Constants.LOAD_TYPE_ALWAYS.equals(mAvatarLoadType)
                || (!isMobileNetwork() && Constants.LOAD_TYPE_ONLY_WIFI.equals(mAvatarLoadType));
    }

    // --------------- THIS IS NOT IN PERF -----------

    public void setLastCheckSmsTime(long lastCheckSmsTime) {
        mLastCheckSmsTime = lastCheckSmsTime;
    }

    public boolean isCheckSms() {
        return System.currentTimeMillis() > mLastCheckSmsTime + 30 * 1000;
    }

    public void reload() {
        getUsernameFromPref();
        getPasswordFromPref();
        getUidFromPref();
        getSecQuestionFromPref();
        getSecAnswerFromPref();
        isShowStickThreadsFromPref();
        getAvatarLoadTypeFromPref();
        isSortByPostTimeByForumFromPref();
        isAddTailFromPref();
        getTailTextFromPref();
        getTailUrlFromPref();
        getThemeFromPref();
        getPrimaryColorFromPref();
        getNightThemeFromPref();
        isNightModeFromPref();
        isNavBarColoredFromPref();
        getFontFromPref();
        isEncodeUtf8FromPref();
        getPostTextSizeAdjFromPref();
        getTitleTextSizeAdjFromPref();
        getScreenOrientationFromPref();
        isGestureBackFromPref();
        getPostLineSpacingFromPref();
        getLastForumIdFromPerf();
        isErrorReportModeFromPref();
        getForumsFromPref();
        getFreqMenusFromPref();
        isNotiTaskEnabledFromPref();
        getBSTypeIdFromPref();
        getForumServerFromPref();
        getImageHostFromPref();
        getOldBlacklistsFromPref();
        getBlacklistsFromPref();

        updateMobileNetworkStatus(mCtx);
    }

    public boolean isLoginInfoValid() {
        return (!mUsername.isEmpty() && !mPassword.isEmpty());
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_USERNAME, username).apply();
    }

    private void getUsernameFromPref() {
        mUsername = mSharedPref.getString(PERF_USERNAME, "");
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_PASSWORD, password).apply();
    }

    private void getPasswordFromPref() {
        mPassword = mSharedPref.getString(PERF_PASSWORD, "");
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_UID, uid).apply();
    }

    private void getUidFromPref() {
        mUid = mSharedPref.getString(PERF_UID, "");
    }

    public String getSecQuestion() {
        return mSecQuestion;
    }

    public void setSecQuestion(String secQuestion) {
        mSecQuestion = secQuestion;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_SECQUESTION, secQuestion).apply();
    }

    private void getSecQuestionFromPref() {
        mSecQuestion = mSharedPref.getString(PERF_SECQUESTION, "");
    }

    public String getSecAnswer() {
        return mSecAnswer;
    }

    public void setSecAnswer(String secAnswer) {
        mSecAnswer = secAnswer;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_SECANSWER, secAnswer).apply();
    }

    private void getSecAnswerFromPref() {
        mSecAnswer = mSharedPref.getString(PERF_SECANSWER, "");
    }

    public boolean isShowStickThreads() {
        return mShowStickThreads;
    }

    public void setShowStickThreads(boolean showStickThreads) {
        mShowStickThreads = showStickThreads;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(PERF_SHOWSTICKTHREADS, showStickThreads).apply();
    }

    private void isShowStickThreadsFromPref() {
        mShowStickThreads = mSharedPref.getBoolean(PERF_SHOWSTICKTHREADS, false);
    }

    private void getAvatarLoadTypeFromPref() {
        mAvatarLoadType = mSharedPref.getString(PERF_AVATAR_LOAD_TYPE, Constants.LOAD_TYPE_ALWAYS);
    }

    public boolean isSortByPostTime(int fid) {
        return mSortByPostTimeByForum.contains(fid + "");
    }

    public void setSortByPostTime(int fid, boolean sortByPostTime) {
        if (sortByPostTime) {
            mSortByPostTimeByForum.add(fid + "");
        } else {
            mSortByPostTimeByForum.remove(fid + "");
        }
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.remove(PERF_SORTBYPOSTTIME_BY_FORUM).apply();
        editor.putStringSet(PERF_SORTBYPOSTTIME_BY_FORUM, mSortByPostTimeByForum).apply();
    }

    private void isSortByPostTimeByForumFromPref() {
        mSortByPostTimeByForum = mSharedPref.getStringSet(PERF_SORTBYPOSTTIME_BY_FORUM, new HashSet<>());
    }

    public boolean isAddTail() {
        return mAddTail && Utils.getWordCount(mTailText) <= MAX_TAIL_TEXT_LENGTH;
    }

    public void isAddTailFromPref() {
        mAddTail = mSharedPref.getBoolean(PERF_ADDTAIL, false);
    }

    public String getTailText() {
        return mTailText;
    }

    private void getTailTextFromPref() {
        mTailText = mSharedPref.getString(PERF_TAILTEXT, "");
    }

    public String getTailUrl() {
        return mTailUrl;
    }

    private void getTailUrlFromPref() {
        mTailUrl = mSharedPref.getString(PERF_TAILURL, "");
    }

    public String getTailStr() {
        String tailStr = "";
        String tailText = getTailText().trim();
        if (!TextUtils.isEmpty(tailText)) {
            String tailUrl = getTailUrl();
            if (!TextUtils.isEmpty(tailUrl)) {
                if ((!tailUrl.startsWith("http")) && (!tailUrl.startsWith("https"))) {
                    tailUrl = "http://" + tailUrl;
                }
                tailStr = "[url=" + tailUrl + "][size=1]" + tailText + "[/size][/url]";
            } else {
                tailStr = "[size=1]" + tailText + "[/size]";
            }
        }
        return tailStr;
    }

    public String getTheme() {
        return mTheme;
    }

    public void setTheme(String theme) {
        mTheme = theme;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_THEME, theme).apply();
    }

    private void getThemeFromPref() {
        mTheme = mSharedPref.getString(PERF_THEME, THEME_LIGHT);
    }

    public int getPrimaryColor() {
        return mPrimaryColor;
    }

    public void setPrimaryColor(int primaryColor) {
        mPrimaryColor = primaryColor;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt(PERF_PRIMARY_COLOR, primaryColor).apply();
    }

    private void getPrimaryColorFromPref() {
        mPrimaryColor = mSharedPref.getInt(PERF_PRIMARY_COLOR, 0);
    }

    public String getNightTheme() {
        return mNightTheme;
    }

    private void getNightThemeFromPref() {
        mNightTheme = mSharedPref.getString(PERF_NIGHT_THEME, THEME_DARK);
    }

    public boolean isNightMode() {
        return mNightMode;
    }

    @SuppressLint("ApplySharedPref")
    public void setNightMode(boolean nightMode) {
        mNightMode = nightMode;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(PERF_NIGHT_MODE, nightMode).commit();
    }

    private void isNightModeFromPref() {
        mNightMode = mSharedPref.getBoolean(PERF_NIGHT_MODE, false);
    }

    public String getFont() {
        return mFont;
    }

    public void setFont(String font) {
        mFont = font;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_FONT, font).apply();
    }

    private void getFontFromPref() {
        mFont = mSharedPref.getString(PERF_FONT, "");
    }

    public boolean isNavBarColored() {
        return mNavBarColor;
    }

    private void isNavBarColoredFromPref() {
        mNavBarColor = mSharedPref.getBoolean(PERF_NAVBAR_COLORED, false);
    }

    public List<Integer> getForums() {
        return mForums;
    }

    public void setForums(List<Integer> forums) {
        if (forums.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int fid : forums) {
                sb.append(fid).append(",");
            }
            mForums = forums;
            SharedPreferences.Editor editor = mSharedPref.edit();
            editor.remove(PERF_FORUMS).apply();
            editor.putString(PERF_FORUMS, sb.toString()).apply();
        }
    }

    private void getForumsFromPref() {
        List<Integer> forums = new ArrayList<>();
        String fidsAsString = mSharedPref.getString(PERF_FORUMS, "");
        String[] fids = fidsAsString.split(",");
        for (String fid : fids) {
            if (HiUtils.isValidId(fid) && HiUtils.getForumByFid(Integer.parseInt(fid)) != null)
                forums.add(Integer.valueOf(fid));
        }
        if (forums.size() == 0) {
            for (int fid : HiUtils.DEFAULT_FORUMS) {
                forums.add(fid);
            }
        }
        mForums = forums;
    }

    public Set<String> getFreqMenus() {
        return mFreqMenus;
    }

    private void getFreqMenusFromPref() {
        mFreqMenus = mSharedPref.getStringSet(PERF_FREQ_MENUS, new HashSet<>());
    }

    private void isEncodeUtf8FromPref() {
        mEncodeUtf8 = mSharedPref.getBoolean(PERF_ENCODEUTF8, false);
    }

    public String getEncode() {
        if (mEncodeUtf8) {
            return "UTF-8";
        } else {
            return "GBK";
        }
    }

    public boolean isErrorReportMode() {
        return mErrorReportMode;
    }

    private void isErrorReportModeFromPref() {
        mErrorReportMode = mSharedPref.getBoolean(PERF_ERROR_REPORT_MODE, false);
    }

    public boolean isNotiTaskEnabled() {
        return mNotiTaskEnabled;
    }

    private void isNotiTaskEnabledFromPref() {
        mNotiTaskEnabled = mSharedPref.getBoolean(PERF_NOTI_TASK_ENABLED, false);
    }

    public List<String> getOldBlacklists() {
        return mOldBlocklists;
    }

    public void setOldBlacklists(List<String> blacklists) {
        mOldBlocklists = blacklists;
        StringBuilder sb = new StringBuilder();
        for (String username : blacklists) {
            if (!TextUtils.isEmpty(username)) {
                if (sb.length() > 0)
                    sb.append("\n");
                sb.append(username);
            }
        }
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_OLD_BLACKLIST, sb.toString()).apply();
    }

    private void getOldBlacklistsFromPref() {
        String[] usernames = mSharedPref.getString(PERF_OLD_BLACKLIST, "").split("\n");
        mOldBlocklists = new ArrayList<>();
        for (String username : usernames) {
            if (!TextUtils.isEmpty(username) && !mOldBlocklists.contains(username))
                mOldBlocklists.add(username);
        }
    }

    public List<String> getBlacklists() {
        if (mBlocklists == null)
            mBlocklists = new ArrayList<>();
        return mBlocklists;
    }

    public void setBlacklists(List<String> blacklists) {
        mBlocklists = blacklists;
        StringBuilder sb = new StringBuilder();
        for (String username : blacklists) {
            if (!TextUtils.isEmpty(username)) {
                if (sb.length() > 0)
                    sb.append("\n");
                sb.append(username);
            }
        }
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_BLACKLIST, sb.toString()).apply();
    }

    private void getBlacklistsFromPref() {
        String[] usernames = mSharedPref.getString(PERF_BLACKLIST, "").split("\n");
        mBlocklists = new ArrayList<>();
        for (String username : usernames) {
            if (!TextUtils.isEmpty(username) && !mBlocklists.contains(username))
                mBlocklists.add(username);
        }
    }

    public boolean notInBlocklist(String username) {
        return !mBlocklists.contains(username) && !mOldBlocklists.contains(username);
    }

    public void addToBlacklist(String username) {
        if (!TextUtils.isEmpty(username) && !mBlocklists.contains(username)) {
            mBlocklists.add(username);
            setBlacklists(mBlocklists);
        }
    }

    public void removeFromBlacklist(String username) {
        if (!TextUtils.isEmpty(username)) {
            mBlocklists.remove(username);
            setBlacklists(mBlocklists);
        }
    }

    public int getPostTextSizeAdj() {
        return mPostTextSizeAdj;
    }

    public void setPostTextSizeAdj(int adj) {
        mPostTextSizeAdj = adj;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_TEXTSIZE_POST_ADJ, String.valueOf(adj)).apply();
    }

    private void getPostTextSizeAdjFromPref() {
        mPostTextSizeAdj = Utils.parseInt(mSharedPref.getString(PERF_TEXTSIZE_POST_ADJ, "0"));
    }

    public int getPostLineSpacing() {
        return mPostLineSpacing;
    }

    public void setPostLineSpacing(int lineSpacing) {
        mPostLineSpacing = lineSpacing;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_POST_LINE_SPACING, lineSpacing + "").apply();
    }

    private void getPostLineSpacingFromPref() {
        String value = mSharedPref.getString(PERF_POST_LINE_SPACING, "0");
        if (TextUtils.isDigitsOnly(value)) {
            mPostLineSpacing = Integer.parseInt(value);
        }
    }

    public int getTitleTextSizeAdj() {
        return mTitleTextSizeAdj;
    }

    public void setTitleTextSizeAdj(int adj) {
        mTitleTextSizeAdj = adj;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_TEXTSIZE_TITLE_ADJ, String.valueOf(adj)).apply();
    }

    private void getTitleTextSizeAdjFromPref() {
        mTitleTextSizeAdj = Utils.parseInt(mSharedPref.getString(PERF_TEXTSIZE_TITLE_ADJ, "0"));
    }

    public int getScreenOrientation() {
        return mScreenOrientation;
    }

    private void getScreenOrientationFromPref() {
        try {
            mScreenOrientation = Integer.parseInt(mSharedPref.getString(PERF_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_USER + ""));
        } catch (Exception e) {
            mScreenOrientation = ActivityInfo.SCREEN_ORIENTATION_USER;
        }
    }

    public boolean isGestureBack() {
        return mGestureBack;
    }

    private void isGestureBackFromPref() {
        mGestureBack = mSharedPref.getBoolean(PERF_GESTURE_BACK, true);
    }

    public Date getLastUpdateCheckTime() {
        String millis = mSharedPref.getString(PERF_LAST_UPDATE_CHECK, "");
        if (!TextUtils.isEmpty(millis) && TextUtils.isDigitsOnly(millis)) {
            try {
                return new Date(Long.parseLong(millis));
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public void setLastUpdateCheckTime(Date d) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(HiSettingsHelper.PERF_LAST_UPDATE_CHECK, d.getTime() + "").apply();
    }

    public boolean isAutoUpdateCheck() {
        return mSharedPref.getBoolean(PERF_AUTO_UPDATE_CHECK, true);
    }

    public void setAutoUpdateCheck(boolean b) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(HiSettingsHelper.PERF_AUTO_UPDATE_CHECK, b).apply();
    }

    public int getMaxPostsInPage() {
        if (mMaxPostsInPage <= 0) {
            mMaxPostsInPage = mSharedPref.getInt(PERF_MAX_POSTS_IN_PAGE, 50);
        }
        return mMaxPostsInPage;
    }

    public void setMaxPostsInPage(int maxPostsInPage) {
        //could be 5,10,15 default is 50
        if (maxPostsInPage > 0 && maxPostsInPage % 5 == 0 && maxPostsInPage != mMaxPostsInPage) {
            mMaxPostsInPage = maxPostsInPage;
            SharedPreferences.Editor editor = mSharedPref.edit();
            editor.putInt(HiSettingsHelper.PERF_MAX_POSTS_IN_PAGE, mMaxPostsInPage).apply();
        }
    }

    public int getLastForumId() {
        return mLastForumId;
    }

    public void setLastForumId(int fid) {
        mLastForumId = fid;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt(PERF_LAST_FORUM_ID, fid).apply();
    }

    private void getLastForumIdFromPerf() {
        mLastForumId = mSharedPref.getInt(PERF_LAST_FORUM_ID, 0);
    }

    public String getBSTypeId() {
        return mBSTypeId;
    }

    public void setBSTypeId(String typeId) {
        mBSTypeId = typeId;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_BS_TYPE_ID, typeId).apply();
    }

    private void getBSTypeIdFromPref() {
        mBSTypeId = mSharedPref.getString(PERF_BS_TYPE_ID, "");
    }

    public boolean isAutoUpdateCheckable() {
        if (!isAutoUpdateCheck() || Utils.isFromGooglePlay(mCtx))
            return false;
        Date lastCheck = HiSettingsHelper.getInstance().getLastUpdateCheckTime();
        //check update if last check is older than 12 hours
        return lastCheck == null
                || System.currentTimeMillis() > lastCheck.getTime() + 12 * 60 * 60 * 1000;
    }

    public String getInstalledVersion() {
        return mSharedPref.getString(PERF_INSTALLED_VERSION, "");
    }

    public void setInstalledVersion(String version) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(HiSettingsHelper.PERF_INSTALLED_VERSION, version).apply();
    }

    public int getPostTextSize() {
        if (mBasePostTextSize <= 0)
            mBasePostTextSize = mCtx.getResources().getInteger(R.integer.post_text_size);
        return mBasePostTextSize + getInstance().getPostTextSizeAdj();
    }

    public int getTitleTextSize() {
        if (mBaseTitleTextSize <= 0)
            mBaseTitleTextSize = mCtx.getResources().getInteger(R.integer.title_text_size);
        return mBaseTitleTextSize + getInstance().getTitleTextSizeAdj();
    }

    public String getForumServer() {
        return mForumServer;
    }

    public void setForumServer(String forumServer) {
        mForumServer = forumServer;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_FORUM_SERVER, mForumServer).apply();
    }

    private void getForumServerFromPref() {
        mForumServer = mSharedPref.getString(PERF_FORUM_SERVER, HiUtils.ForumServer);
    }

    public String getImageHost() {
        return mImageHost;
    }

    public void setImageHost(String imageHost) {
        mImageHost = imageHost;
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PERF_IMAGE_HOST, mImageHost).apply();
    }

    private void getImageHostFromPref() {
        mImageHost = mSharedPref.getString(PERF_IMAGE_HOST, HiUtils.ImageHost);
    }

    public String getStringValue(String key, String defaultValue) {
        return mSharedPref.getString(key, defaultValue);
    }

    public void setStringValue(String key, String value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(key, value).apply();
    }

    public long getLongValue(String key, long defaultValue) {
        return mSharedPref.getLong(key, defaultValue);
    }

    public void setLongValue(String key, long value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putLong(key, value).apply();
    }

    public int getIntValue(String key, int defaultValue) {
        return mSharedPref.getInt(key, defaultValue);
    }

    public void setIntValue(String key, int value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt(key, value).apply();
    }

    public boolean getBooleanValue(String key, boolean defaultValue) {
        return mSharedPref.getBoolean(key, defaultValue);
    }

    public void setBooleanValue(String key, boolean value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(key, value).apply();
    }

    public boolean isInSilentMode() {
        return mSharedPref.getBoolean(PERF_NOTI_SILENT_MODE, false)
                && Utils.isInTimeRange(getSilentBegin(), getSilentEnd());
    }

    public String getActiveTheme() {
        if (isNightMode() && !TextUtils.isEmpty(getNightTheme()))
            return getNightTheme();
        else
            return getTheme();
    }

    public boolean isUsingLightTheme() {
        return HiSettingsHelper.THEME_LIGHT.equals(getActiveTheme());
    }

    public boolean isAppBarCollapsible() {
        return getBooleanValue(PERF_APP_BAR_COLLAPSIBLE, true);
    }

    public boolean isFabLeftSide() {
        return getBooleanValue(PERF_FAB_LEFT_SIDE, false);
    }

    public boolean isFabAutoHide() {
        return getBooleanValue(PERF_FAB_AUTO_HIDE, true);
    }

    public boolean isTrustAllCerts() {
        return getBooleanValue(PERF_TRUST_ALL_CERTS, false);
    }

    public boolean isClickEffect() {
        return getBooleanValue(PERF_CLICK_EFFECT, true);
    }

    public boolean isShowTail() {
        return getBooleanValue(PERF_SHOW_TAIL, false);
    }

    public int getMaxUploadFileSize() {
        return getIntValue(PERF_MAX_UPLOAD_FILE_SIZE, HiUtils.DEFAULT_MAX_UPLOAD_FILE_SIZE);
    }

    public void setMaxUploadFileSize(int fileSize) {
        setIntValue(PERF_MAX_UPLOAD_FILE_SIZE, fileSize);
    }

    public boolean isCircleAvatar() {
        return getBooleanValue(PERF_CIRCLE_AVATAR, true);
    }

    public boolean isCameraPermAsked() {
        return getBooleanValue(PERF_CAMERA_PERM_ASKED, false);
    }

    public void setCameraPermAsked(boolean asked) {
        setBooleanValue(PERF_CAMERA_PERM_ASKED, asked);
    }

    public boolean isWhiteTheme() {
        return THEME_LIGHT.equals(getActiveTheme())
                && getPrimaryColor() == ContextCompat.getColor(mCtx, R.color.md_grey_200);
    }

    public int getToolbarTextColor() {
        return isWhiteTheme() ? Color.BLACK : Color.WHITE;
    }

    public int getImageActivityTheme(Activity activity) {
        if (isWhiteTheme()) {
            return R.style.Matisse_Zhihu;
        }
        return HiUtils.getThemeValue(activity,
                HiSettingsHelper.getInstance().getActiveTheme(),
                HiSettingsHelper.getInstance().getPrimaryColor());
    }

    public String getSilentBegin() {
        return getStringValue(
                HiSettingsHelper.PERF_NOTI_SILENT_BEGIN,
                NotiHelper.DEFAUL_SLIENT_BEGIN);
    }

    public String getSilentEnd() {
        return getStringValue(
                HiSettingsHelper.PERF_NOTI_SILENT_END,
                NotiHelper.DEFAUL_SLIENT_END);
    }

    public Date getBlacklistSyncTime() {
        long millis = getLongValue(PERF_BLACKLIST_SYNC_TIME, 0);
        if (millis > 0) {
            try {
                return new Date(millis);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public void setBlacklistSyncTime() {
        setLongValue(PERF_BLACKLIST_SYNC_TIME, System.currentTimeMillis());
    }

    public Date getNotiJobLastRunTime() {
        long millis = getLongValue(PERF_NOTI_JOB_LAST_TIME, 0);
        if (millis > 0) {
            try {
                return new Date(millis);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public void setNotiJobLastRunTime() {
        setLongValue(PERF_NOTI_JOB_LAST_TIME, System.currentTimeMillis());
    }

    private static class SingletonHolder {
        public static final HiSettingsHelper INSTANCE = new HiSettingsHelper();
    }

}
