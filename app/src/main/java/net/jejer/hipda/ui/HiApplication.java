package net.jejer.hipda.ui;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import net.jejer.hipda.R;
import net.jejer.hipda.async.UpdateHelper;
import net.jejer.hipda.bean.HiSettingsHelper;
import net.jejer.hipda.utils.HiUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by GreenSkinMonster on 2015-03-28.
 */
public class HiApplication extends Application implements Application.ActivityLifecycleCallbacks {

    public final static int IDLE = 0;
    public final static int RELOAD = 1;
    public final static int RECREATE = 2;
    public final static int RESTART = 3;

    private static Context context;
    private static boolean notified;
    private static boolean updated;
    private static boolean fontSet;
    private static int settingStatus;
    private static int visibleActivityCount = 0;
    private static int mainActivityCount = 0;

    public static Context getAppContext() {
        return HiApplication.context;
    }

    public static String getAppVersion() {
        String version = "0.0.00";
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception ignored) {
        }
        return version;
    }

    public static boolean isNotified() {
        return notified;
    }

    public static void setNotified(boolean b) {
        notified = b;
    }

    public static boolean isUpdated() {
        return updated;
    }

    public static void setUpdated(boolean updated) {
        HiApplication.updated = updated;
    }

    public static boolean isFontSet() {
        return fontSet;
    }

    public static int getSettingStatus() {
        return settingStatus;
    }

    public static void setSettingStatus(int settingStatus) {
        HiApplication.settingStatus = settingStatus;
    }

    public static boolean isAppVisible() {
        return visibleActivityCount > 0;
    }

    public static int getMainActivityCount() {
        return mainActivityCount;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        registerActivityLifecycleCallbacks(this);

        updated = UpdateHelper.updateApp();

        String font = HiSettingsHelper.getInstance().getFont();
        if (new File(font).exists()) {
            fontSet = true;
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath(font)
                    .setFontAttrId(R.attr.fontPath)
                    .build()
            );
        } else {
            HiSettingsHelper.getInstance().setFont("");
        }
        HiUtils.updateBaseUrls();
    }

    public void onActivityCreated(@NotNull Activity activity, Bundle bundle) {
        if (activity instanceof MainFrameActivity) {
            mainActivityCount++;
        }
    }

    public void onActivityDestroyed(@NotNull Activity activity) {
        if (activity instanceof MainFrameActivity) {
            mainActivityCount--;
        }
    }

    public void onActivityResumed(@NotNull Activity activity) {
    }

    public void onActivityPaused(@NotNull Activity activity) {
    }


    public void onActivitySaveInstanceState(@NotNull Activity activity, @NotNull Bundle outState) {
    }

    public void onActivityStarted(@NotNull Activity activity) {
        visibleActivityCount++;
    }

    public void onActivityStopped(@NotNull Activity activity) {
        visibleActivityCount--;
    }

}
