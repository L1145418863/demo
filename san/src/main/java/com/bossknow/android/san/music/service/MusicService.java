package com.bossknow.android.san.music.service;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bossknow.android.san.R;
import com.bossknow.android.san.activity.Main2Activity;
import com.bossknow.android.san.activity.MainActivity;
import com.bossknow.android.san.music.bean.ServiceAllStatus;
import com.bossknow.android.san.music.serviceinterface.ChangedSeekBar;
import com.bossknow.android.san.music.serviceinterface.OnMediaPlayerStatus;
import com.bossknow.android.san.music.util.MusicManage;
import com.bossknow.android.san.music.util.TimeUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 音乐播放器的Service
 */
public class MusicService extends Service {
    //变量区
    public int status = 0;//音乐播放器的状态
    public String path = "";//音乐播放器播放的音频地址
    public String musicName = "";//音乐播放器播放的音频名
    public Map<String, String> headers;//音乐播放器播放的请求头
    public int totalSecond = 0;//音频总时长(秒)
    public int broadcastSecond = 0;//音频已播时长(秒)
    public int totalLength = 0;//音频总时长(毫秒)
    public int broadcastTime = 0;//音频已播时长(毫秒)
    public String totalLengthSTR = "00:00";//总时长格式化
    public String broadcastTimeSTR = "00:00";//已播时长格式化
    public int oneSecond = 1000;//倍速时一秒等于的毫秒数
    public float speed = 1.0f;//倍速
    public int seekBarMAX = 0;//SeekBar最大进度
    public boolean autoplay = false;//是否加载完就进行播放
    //常量区
    public static final int MEDIA_STATUS_NOTINIT = 0;//mediaplayer尚未初始化
    public static final int MEDIA_STATUS_ALREADY = 1;//mediaplayer已准备好
    public static final int MEDIA_STATUS_LOADER = 2;//mediaplayer正在加载
    public static final int MEDIA_STATUS_URL_NULL = 3;//mediaplayer播放地址为null
    public static final int MEDIA_STATUS_NOTSTART = 4;//mediaplayer尚未开始
    public static final int MEDIA_STATUS_IS_TSTART = 5;//mediaplayer正在播放
    public static final int MEDIA_STATUS_DO_PAUSE = 6;//mediaplayer处于即将暂停状态
    public static final int MEDIA_STATUS_IS_PAUSE = 7;//mediaplayer处于暂停状态
    public static final int MEDIA_STATUS_IS_COMPLETE = 8;//mediaplayer播放完成
    public static final int MEDIA_STATUS_IS_ERROR = 9;//mediaplayer播放出错
    public static final int MEDIA_STATUS_NOT = 10;//mediaplayer无效状态
    private static final float MEDIA_SPEED_1_0 = 1.0f;//一倍速
    private static final float MEDIA_SPEED_1_25 = 1.25f;//一点二倍速
    private static final float MEDIA_SPEED_1_5 = 1.5f;//一点五倍速
    private static final float MEDIA_SPEED_2_0 = 2.0f;//二倍速
    private NotificationCompat.Builder builder;
    private RemoteViews remoteBig;
    private RemoteViews remoteSmall;

    @Override
    public IBinder onBind(Intent intent) {
        ChangedStatus(MEDIA_STATUS_NOTINIT);
        MusicManage.context = this;
        MusicManage.isDestroy = false;
        return new MusicServiceBinder();
    }

    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓业务逻辑区域↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    //进度变化
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (MusicManage.isDestroy) {
                handler.removeCallbacksAndMessages(null);
                return false;
            }
            if (status == MEDIA_STATUS_DO_PAUSE) {
                handler.removeCallbacksAndMessages(null);
                Pause();
                return false;
            }
            broadcastSecond++;
            if (broadcastSecond <= totalSecond) {
                handler.sendEmptyMessageDelayed(1, oneSecond);
                broadcastTimeSTR = TimeUtil.getTime(broadcastSecond);
                broadcastTime = broadcastSecond * 1000;
                SeekBarProgrossChanged(broadcastSecond);
                doSendNotification();
            } else {
                handler.removeCallbacksAndMessages(null);
                ChangedStatus(MEDIA_STATUS_IS_COMPLETE);
            }
            return false;
        }
    });

    /**
     * @param path 音频地址
     */
    public void setUp(Activity activity, String path, String musicName) {
        Map<String, String> headers = new HashMap<>();
        headers.put("key", "value");
        this.setUp(activity, path, musicName, headers);
    }

    /**
     * @param path    音频地址
     * @param headers 音频请求头(阿里云 云文件加密方式之一)
     */
    public void setUp(Activity activity, String path, String musicName, Map<String, String> headers) {
        MusicManage.activity = activity;
        if (path.equals(this.path)) {
            return;
        } else {
            FreedMedia();
        }
        this.path = path;
        this.headers = headers;
        this.musicName = musicName;
        if (TextUtils.isEmpty(path)) {
            ChangedStatus(MEDIA_STATUS_URL_NULL);
        } else {//有效播放地址
            try {
                MusicManage.mediaPlayer = new MediaPlayer();
                initMediaPlayerListener();
                Uri parse = Uri.parse(path);//生成地址
                MusicManage.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//播放声音的类型
                MusicManage.mediaPlayer.setDataSource(MusicManage.context, parse, headers);//请求
                MusicManage.mediaPlayer.prepareAsync();//异步加载
                ChangedStatus(MEDIA_STATUS_LOADER);
                MusicManage.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {//异步加载必须使用此监听才能拿到正确的时长
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        ChangedStatus(MEDIA_STATUS_NOTSTART);
                        totalLength = MusicManage.mediaPlayer.getDuration();//获取时长毫秒
                        seekBarMAX = totalSecond = totalLength / 1000;//获取时长秒和seekbar的最大长度
                        broadcastSecond = 0;
                        SeekBarMAXChanged(seekBarMAX);
                        SeekBarProgrossChanged(broadcastSecond);
//                        MediaPlayOrPause();//测试用
                        if (autoplay) {
                            status = MEDIA_STATUS_NOTSTART;
                            MediaPlayOrPause();
                        }
                    }
                });
            } catch (IOException e) {
                ChangedStatus(MEDIA_STATUS_IS_ERROR);
                e.printStackTrace();
            }
        }
    }

    /**
     * 音频开始或暂停播放
     */
    public void MediaPlayOrPause() {
        if (status == MEDIA_STATUS_NOTINIT || status == MEDIA_STATUS_ALREADY || status == MEDIA_STATUS_URL_NULL) {
            Toast.makeText(this, "无效播放地址", Toast.LENGTH_SHORT).show();
        } else if (status == MEDIA_STATUS_NOTSTART || status == MEDIA_STATUS_IS_COMPLETE || status == MEDIA_STATUS_IS_PAUSE) {
            Start();
        } else if (status == MEDIA_STATUS_IS_TSTART) {
            DoPause();
        } else if (status == MEDIA_STATUS_IS_ERROR) {
            this.setUp(MusicManage.activity, path, musicName, headers);
        } else if (status == MEDIA_STATUS_DO_PAUSE) {
            ChangedStatus(MEDIA_STATUS_DO_PAUSE);
        } else {
            ChangedStatus(MEDIA_STATUS_NOT);
        }
    }

    /**
     * 开始
     */
    public void Start() {
        if (status == MEDIA_STATUS_IS_COMPLETE) {
            broadcastSecond = 0;
        }
        ChangedStatus(MEDIA_STATUS_IS_TSTART);
        MusicManage.mediaPlayer.start();
        handler.removeCallbacksAndMessages(null);
        handler.sendEmptyMessageDelayed(1, 0);
    }

    /**
     * 执行暂停操作 并未暂停
     */
    private void DoPause() {
        ChangedStatus(MEDIA_STATUS_DO_PAUSE);
    }

    /**
     * 暂停
     */
    public void Pause() {
        ChangedStatus(MEDIA_STATUS_IS_PAUSE);
        MusicManage.mediaPlayer.pause();
        handler.removeCallbacksAndMessages(null);
    }

    /**
     * 初始化MediaPlayer 的监听
     */
    private void initMediaPlayerListener() {
        //播放出错
        MusicManage.mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                ChangedStatus(MEDIA_STATUS_IS_ERROR);
                return false;
            }
        });
        //播放完成
        MusicManage.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                handler.removeCallbacksAndMessages(null);
                ChangedStatus(MEDIA_STATUS_IS_COMPLETE);
            }
        });
        MusicManage.mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                if (mp.isPlaying()) {
                    Log.e("缓存进度", "percent:" + percent);
                    SeekBarseekSecondaryProgressChanged(percent);
                }
            }
        });
    }

    /**
     * 释放mediaplayer
     */
    private void FreedMedia() {
        if (MusicManage.mediaPlayer != null) {
            if (MusicManage.mediaPlayer.isPlaying()) {
                MusicManage.mediaPlayer.stop();
            }
            MusicManage.mediaPlayer.release();
            MusicManage.mediaPlayer = null;
        }
    }
    //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑业务逻辑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓获取service的数据↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    /**
     * @return 播放器所有状态
     */
    public ServiceAllStatus getAllStatus() {
        return new ServiceAllStatus(status, path, musicName, totalSecond, broadcastSecond, totalLength, broadcastTime, totalLengthSTR, broadcastTimeSTR, oneSecond, speed, seekBarMAX, autoplay);
    }

    /**
     * @return 获取播放状态
     */
    public int getStatus() {
        return status;
    }

    /**
     * @return 获取倍速值
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * @return 获取音乐名
     */
    public String getMusicName() {
        return musicName;
    }

    /**
     * @return 获取总时长(格式化后的秒数)
     */
    public String getTotalLengthSTR() {
        return totalLengthSTR;
    }

    /**
     * @return 获取总时长(毫秒值)
     */
    public int getTotalLength() {
        return totalLength;
    }

    /**
     * @return 获取当前播放进度(格式化后的秒数)
     */
    public String getBroadcastTimeSTR() {
        return broadcastTimeSTR;
    }

    /**
     * @return 获取当前播放进度(毫秒值)
     */
    public int getBroadcastTime() {
        return broadcastTime;
    }

    //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑获取service的数据↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓统一改变区↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    /**
     * @param status mediaplayer的状态
     */
    public void ChangedStatus(int status) {
        this.status = status;
        if (status != MEDIA_STATUS_NOTINIT && status != MEDIA_STATUS_ALREADY && status != MEDIA_STATUS_URL_NULL && status != MEDIA_STATUS_URL_NULL) {
            doSendNotification();
        }
        if (onMediaPlayerStatus != null) {
            onMediaPlayerStatus.mediaPlayerStatus(status);
        }
    }

    /**
     * @param seekBarMAX 音频总时长变化
     */
    public void SeekBarMAXChanged(int seekBarMAX) {
        totalLengthSTR = TimeUtil.getTime(seekBarMAX);
        if (changedSeekBar != null) {
            changedSeekBar.seekBarMaxChanged(seekBarMAX, totalLengthSTR, musicName);
        }
    }

    /**
     * @param progross 音频播放进度变化
     */
    public void SeekBarProgrossChanged(int progross) {
        if (changedSeekBar != null) {
            changedSeekBar.seekBarProgrossChanged(progross, TimeUtil.getTime(progross));
        }
    }

    /**
     * @param progross 音频播放进度变化
     */
    public void SeekBarseekSecondaryProgressChanged(int progross) {
        if (changedSeekBar != null) {
            changedSeekBar.seekBarsSecondaryProgressChanged(progross);
        }
    }

    //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑统一改变区↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓接口区域↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    OnMediaPlayerStatus onMediaPlayerStatus;

    public void setOnMediaPlayerStatus(OnMediaPlayerStatus onMediaPlayerStatus) {
        this.onMediaPlayerStatus = onMediaPlayerStatus;
    }

    ChangedSeekBar changedSeekBar;

    public void setChangedSeekBar(ChangedSeekBar changedSeekBar) {
        this.changedSeekBar = changedSeekBar;
    }

    public void SeekTo(int progress) {
        broadcastSecond = progress;
        SeekBarProgrossChanged(progress);
        MusicManage.mediaPlayer.seekTo(progress * 1000);
        ChangedStatus(MEDIA_STATUS_IS_PAUSE);
        MediaPlayOrPause();
    }

    //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑接口区域↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓Service区域↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    /**
     * bindService时获取Service对象
     */
    public class MusicServiceBinder extends Binder {
        public MusicService getMusicService() {
            return new MusicService();
        }
    }

    @Override
    public void onDestroy() {//用于回收资源
        Recycling();
        super.onDestroy();
    }

    private void Recycling() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        if (MusicManage.mediaPlayer != null) {
            if (MusicManage.mediaPlayer.isPlaying()) {
                MusicManage.mediaPlayer.stop();
            }
            MusicManage.mediaPlayer.release();
            MusicManage.mediaPlayer = null;
        }
        MusicManage.isDestroy = true;
        doSendNotification();
    }


    //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑Service区域↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓通知↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    private void doSendNotification() {
        //创建通知对象
        if (builder == null) {
            builder = new NotificationCompat.Builder(MusicManage.activity, MusicManage.createNotificationChannel(MusicManage.activity));
            builder.setSmallIcon(R.mipmap.ic_launcher);//图标
            //打开程序后图标消失
            builder.setAutoCancel(false);
            //设置不可以侧滑关闭
            builder.setOngoing(true);
            builder.setOnlyAlertOnce(true);
            // 需要VIBRATE权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                builder.setPriority(Notification.PRIORITY_DEFAULT);
            }
            builder.setDefaults(Notification.DEFAULT_VIBRATE);
            //优先级设置为最高
            builder.setPriority(NotificationCompat.PRIORITY_MAX);
            //自定义布局
            remoteBig = new RemoteViews("com.bossknow.android.san", R.layout.notification_big);
            remoteSmall = new RemoteViews("com.bossknow.android.san", R.layout.notification_small);
            //统一消除声音和震动
            builder.setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE);
        }
        //修改数据
        remoteBig.setTextViewText(R.id.notification_name, musicName);
        remoteSmall.setTextViewText(R.id.notification_name, musicName);
        remoteBig.setProgressBar(R.id.notification_progress, seekBarMAX, broadcastSecond, false);
        remoteSmall.setProgressBar(R.id.notification_progress, seekBarMAX, broadcastSecond, false);
        remoteBig.setTextViewText(R.id.notification_start, status + "*");
        remoteSmall.setTextViewText(R.id.notification_start, status + "*");
        //设置事件
        /*Intent previous = new Intent("com.lx.myservicelx.MyReceiver");
        previous.putExtra("zt", "随机状态");
        PendingIntent pi_previous = PendingIntent.getBroadcast(activity, 0,
                previous, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteBig.setOnClickPendingIntent(R.id.notification_icon, pi_previous);
        remoteSmall.setOnClickPendingIntent(R.id.notification_icon, pi_previous);*/
        //设置布局
        builder.setCustomContentView(remoteSmall);   //折叠状态的样式
        builder.setCustomBigContentView(remoteBig);//展开状态的样式  可以不同一个布局
        //管理和设置     不能关闭单独写没有用
        if (MusicManage.notificationManager == null) {
            MusicManage.notificationManager = (NotificationManager) MusicManage.activity.getSystemService(NOTIFICATION_SERVICE);
        }
        Notification build = builder.build();
        build.when = 0;
        if (!MusicManage.isDestroy) {
            //发送通知
            MusicManage.notificationManager.notify(12, build);
        } else {
            //关闭通知
            MusicManage.notificationManager.cancel(12);
        }
    }
    //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑通知↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓进程存活↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

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
    //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑进程存活↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
}
