package com.wu.test;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

public class MusicNotification extends Notification {
    private final static String TAG = "MusicNotification";
    private static MusicNotification notifyInstance = null;
    private Notification musicNotifi = null;
    private final int NOTIFICATION_ID =0;
    // 通知id
    // 布局
    private Context context;
    private final int REQUEST_CODE = 30000;
    int flags= 10001;
    private NotificationManager manager = null;
    private Builder builder = null;
    private final String MUSIC_NOTIFICATION_ACTION_PLAY = "musicnotificaion.To.PLAY";
    private final String MUSIC_NOTIFICATION_ACTION_NEXT = "musicnotificaion.To.NEXT";
    private final String MUSIC_NOTIFICATION_ACTION_PRE = "musicnotificaion.To.Pre";
    private final String MUSIC_NOTIFICATION_ACTION_NPLAY = "musicnotificaion.To.NPLAY";
    private final String MUSIC_NOTIFICATION_ACTION_NNEXT = "musicnotificaion.To.NNEXT";
    private final String MUSIC_NOTIFICATION_ACTION_NPRE = "musicnotificaion.To.NPre";
    private RemoteViews remoteViews;
    private Intent play=null,next=null,pre = null;
    private Intent nplay=null,nnext=null,npre = null;
    private PendingIntent musicPendIntent = null;
    public void setContext(Context context){
        this.context=context;
    }
    public void setManager(NotificationManager manager) {
        this.manager = manager;
    }
    private MusicNotification (Context context){
        this.context = context;
        // 初始化操作
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification);
        builder = new Builder(context);
        play = new Intent();
        play.setAction(MUSIC_NOTIFICATION_ACTION_PLAY);
        next = new Intent();
        next.setAction(MUSIC_NOTIFICATION_ACTION_NEXT);
        pre = new Intent();
        pre.setAction(MUSIC_NOTIFICATION_ACTION_PRE);
        nplay = new Intent();
        nplay.setAction(MUSIC_NOTIFICATION_ACTION_NPLAY);
        nnext = new Intent();
        nnext.setAction(MUSIC_NOTIFICATION_ACTION_NNEXT);
        npre = new Intent();
        npre.setAction(MUSIC_NOTIFICATION_ACTION_NPRE);
    }//恶汉式实现 通知初始化

    public static MusicNotification getMusicNotification(Context context){
        if (notifyInstance == null) {
            notifyInstance = new MusicNotification(context);
        }
        return notifyInstance;
    }


    public void onCreateMusicNotifi() {
        // 设置点击事件

        // 1.注册控制点击事件

        PendingIntent pplay = PendingIntent.getBroadcast(context, REQUEST_CODE,
                play,0);
        remoteViews.setOnClickPendingIntent(R.id.iv_notigication__stopOrStart,
                pplay);

        // 2.注册下一首点击事件

        PendingIntent pnext = PendingIntent.getBroadcast(context, REQUEST_CODE,
                next, 0);
        remoteViews.setOnClickPendingIntent(R.id.iv_notigication__next,
                pnext);

        // 3.注册关闭点击事件

        PendingIntent ppre= PendingIntent.getBroadcast(context, REQUEST_CODE,
                pre, 0);
        remoteViews.setOnClickPendingIntent(R.id.iv_notigication__pre,
                ppre);
        //4.设置点击事件（挑战到播放界面）
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        builder.setContent(remoteViews).setWhen(System.currentTimeMillis())
                // 通知产生的时间，会在通知信息里显示
//              .setPriority(Notification.PRIORITY_DEFAULT)
                // 设置该通知优先级
                .setContentIntent(pendingIntent)
                .setOngoing(true).setTicker("播放新的一首歌")
                .setSmallIcon(R.drawable.music);

        // 兼容性实现

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            musicNotifi = builder.getNotification();
        } else {
            musicNotifi = builder.build();
        }
        musicNotifi.flags = Notification.FLAG_ONGOING_EVENT;
        manager.notify(0, musicNotifi);
//        manager.cancel(0);
    }

    public void onUpdataMusicNotifi(Music song, boolean isplay) {
        // 设置添加内容
        if (song==null){
            remoteViews.setTextViewText(R.id.tv_notigication_songName,"什么东东");
            remoteViews.setTextViewText(R.id.tv_notigication_singer,"未知");
        }
        else {
            remoteViews.setTextViewText(R.id.tv_notigication_songName,
                    (song.getMusic_name()!=null?song.getMusic_name():"什么东东") + "");

            remoteViews.setTextViewText(R.id.tv_notigication_singer,
                    (song.getArtist()!=null?song.getArtist():"未知") + "");

            //判断是否播放
            Log.i(TAG, "onUpdataMusicNotifi: ]"+isplay);
            if (isplay) {
                remoteViews.setImageViewResource(R.id.iv_notigication__stopOrStart,
                        R.drawable.music_play);
            } else {
                remoteViews.setImageViewResource(R.id.iv_notigication__stopOrStart,
                        R.drawable.music_pause);
            }
        }

        onCreateMusicNotifi(); //每一次改变都要重新创建，所以直接写这里
    }

    public void changePlay(){
        remoteViews.setImageViewResource(R.id.iv_notigication__stopOrStart,R.drawable.music_play);
        onCreateMusicNotifi();
    }
    public void changeStop(){
        remoteViews.setImageViewResource(R.id.iv_notigication__stopOrStart,R.drawable.music_pause);
        onCreateMusicNotifi();
    }


    /**
     * 取消通知栏
     */
    public void onCancelMusicNotifi(){
        manager.cancel(0);
    }
}
