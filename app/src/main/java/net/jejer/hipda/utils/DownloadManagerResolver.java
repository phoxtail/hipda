package net.jejer.hipda.utils;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.appcompat.app.AlertDialog;

/**
 * https://gist.github.com/Folyd/b9412bb6e2b06eb511f7
 */
public final class DownloadManagerResolver {

    private static final String DOWNLOAD_MANAGER_PACKAGE_NAME = "com.android.providers.downloads";

    /**
     * Resolve whether the DownloadManager is enable in current devices.
     *
     * @return true if DownloadManager is enable,false otherwise.
     */
    public static boolean resolve(Context context) {
        boolean enable = resolveEnable(context);
        if (!enable) {
            AlertDialog alertDialog = createDialog(context);
            alertDialog.show();
        }
        return enable;
    }

    /**
     * Resolve whether the DownloadManager is enable in current devices.
     *
     * @param context context
     * @return true if DownloadManager is enable,false otherwise.
     */
    private static boolean resolveEnable(Context context) {
        int state = context.getPackageManager()
                .getApplicationEnabledSetting(DOWNLOAD_MANAGER_PACKAGE_NAME);

        return !(state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED ||
                state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED);
    }

    @SuppressLint("RestrictedApi")
    private static AlertDialog createDialog(final Context context) {
        return new AlertDialog.Builder(context)
                .setMessage("下载管理器已停用，请启用")
                .setPositiveButton(context.getResources().getString(android.R.string.ok), (dialog, which) -> enableDownloadManager(context))
                .setCancelable(false)
                .create();
    }

    /**
     * Start activity to enable DownloadManager in Settings.
     */
    private static void enableDownloadManager(Context context) {
        try {
            //Open the specific App Info page:
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + DOWNLOAD_MANAGER_PACKAGE_NAME));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Logger.e(e);
            //Open the generic Apps page:
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            context.startActivity(intent);
        }
    }
}