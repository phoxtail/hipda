package net.jejer.hipda.service;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import net.jejer.hipda.bean.HiSettingsHelper;
import net.jejer.hipda.okhttp.OkHttpHelper;
import net.jejer.hipda.ui.HiApplication;
import net.jejer.hipda.utils.Logger;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

/**
 * Created by GreenSkinMonster on 2019-03-16.
 */
public class NotiWorker extends Worker {
    private static WeakReference<Context> mContext;
    private final static String WORK_NAME = "noti_work";
    private final static int NOTI_REPEAT_MINUTTE = 15;

    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    public NotiWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
        mContext = new WeakReference<>(appContext);
    }

    public static void scheduleOrCancelWork() {
        if (HiSettingsHelper.getInstance().isNotiTaskEnabled()) {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            PeriodicWorkRequest request
                    = new PeriodicWorkRequest
                    .Builder(NotiWorker.class, NOTI_REPEAT_MINUTTE, TimeUnit.MINUTES)
                    .setConstraints(constraints)
                    .build();

            WorkManager.getInstance(mContext.get())
                    .enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, request);
        } else {
            WorkManager.getInstance(mContext.get()).cancelUniqueWork(WORK_NAME);
        }
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            if (!HiApplication.isAppVisible()
                    && OkHttpHelper.getInstance().isLoggedIn()
                    && !HiSettingsHelper.getInstance().isInSilentMode()) {
                HiSettingsHelper.getInstance().setNotiJobLastRunTime();
                NotiHelper.fetchNotification(null);
                NotiHelper.showNotification(HiApplication.getAppContext());
            }
        } catch (Exception e) {
            Logger.e(e);
        }
        return Result.success();
    }
}
