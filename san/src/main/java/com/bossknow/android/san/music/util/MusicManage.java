package com.bossknow.android.san.music.util;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.bossknow.android.san.music.service.MusicService;
import com.bossknow.android.san.music.serviceinterface.AppDestroyInterface;

/**
 * author : LiuZhenGuo Android Developer
 * create by : Administrator -- lx1
 * create date : 2019/7/26
 * create time : 9:52
 * function :
 */
public class MusicManage {
    public static MediaPlayer mediaPlayer = null;
    public static MusicService context = null;
    public static boolean isDestroy = false;
    public static NotificationManager notificationManager;
    public static Activity activity;
    public static MusicService musicService = null;
    public static ServiceConnection sconn = null;
    public static boolean appDestroy = false;

    public static String createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channelId";
            CharSequence channelName = "channelName";
            String channelDescription = "channelDescription";
            int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
            // 设置描述 最长30字符
            notificationChannel.setDescription(channelDescription);
            // 该渠道的通知是否使用震动
            notificationChannel.enableVibration(true);
            // 设置显示模式
            notificationChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
            return channelId;
        } else {
            return null;
        }
    }
}
