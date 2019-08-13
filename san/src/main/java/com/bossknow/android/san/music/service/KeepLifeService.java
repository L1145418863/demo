package com.bossknow.android.san.music.service;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.bossknow.android.san.music.util.MusicManage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class KeepLifeService extends Service {
    private static final String TAG = "KeepLifeService";

    private String mPackName;
    private ActivityManager mActivityManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        String process = getProcessName();
        mPackName = getPackageName();

        boolean isRun = isRunningProcess(mActivityManager, mPackName);

        Log.e("app进程", "process-->:" + process);
        Log.e("app进程", "是否存活-->:" + isRun);
        if (!isRun) {
            try {
                Log.e("app进程", "清理");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("app进程", "" + e);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * @return 获取当前进程名称
     */
    public static String getProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * @return 进程是否存活
     */
    public static boolean isRunningProcess(ActivityManager manager, String processName) {
        if (manager == null)
            return false;
        List<ActivityManager.RunningAppProcessInfo> runnings = manager.getRunningAppProcesses();
        if (runnings != null) {
            for (ActivityManager.RunningAppProcessInfo info : runnings) {
                if (TextUtils.equals(info.processName, processName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
