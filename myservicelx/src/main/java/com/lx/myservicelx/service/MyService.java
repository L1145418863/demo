package com.lx.myservicelx.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.lx.myservicelx.MainActivity;
import com.lx.myservicelx.R;
import com.lx.myservicelx.serviceinterface.OkInterface;
import com.lx.myservicelx.util;

public class MyService extends Service implements OkInterface {
    String hand = "";
    int handNum = 1;
    public final static String ACTION_BUTTON = "com.notifications.intent.action.ButtonClick";
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            handler.sendEmptyMessageDelayed(1000, 1000);
            Log.e(hand, handNum + "S");
            handNum += 1;
            return false;
        }
    });
    Handler handler2 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (!util.isDestroy) {
                handler2.sendEmptyMessageDelayed(20, 1000);
            }
            Log.e(hand, handNum + "S");
            MyTongZhi1();
            handNum += 1;
            return false;
        }
    });
    private Activity activity;
    private NotificationManager notificationManager;

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("MyService", "---onBind---");
        util.isDestroy = false;
        util.okInterface = this;
        return new MyServicMyServiceeBinder();
    }

    @Override
    public void getStatus(int type) {
        Log.e("回调", "得到回调" + type);
    }

    public class MyServicMyServiceeBinder extends Binder {
        public MyService getMyservice() {
            Log.e("MyService", "---获取service对象---");
            handler.sendEmptyMessageDelayed(1000, 1000);
            return new MyService();
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, 1, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        util.isDestroy = true;
        handler.removeCallbacksAndMessages(null);
        handler2.removeCallbacksAndMessages(null);
        Log.e(hand, "handler被终止");

        Log.e("MyService", "---onDestroy---");
    }

    public String getData() {
        Log.e("MyService", "---获取service值---");
        return "service传来的值";
    }

    public void setData(String data) {
        if (myServiceInterface != null) {
            myServiceInterface.ServiceGetDate(true, data + "--yes");
        }
    }

    MyServiceInterface myServiceInterface;

    public void setMyServiceInterface(MyServiceInterface myServiceInterface) {
        this.myServiceInterface = myServiceInterface;
    }

    public interface MyServiceInterface {
        void ServiceGetDate(boolean yesOrNo, String str);
    }

    public void MyTongZhi(Activity activity) {
        this.activity = activity;
        handler2.sendEmptyMessageDelayed(20, 0);
    }

    public void MyTongZhi1() {//通知设置
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity,util.createNotificationChannel(activity));//创建通知对象
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
        RemoteViews remoteViews = new RemoteViews("com.lx.myservicelx", R.layout.notification_layout);
        //修改数据
        remoteViews.setTextViewText(R.id.notification_progress, "Service的通知:" + handNum);
        remoteViews.setProgressBar(R.id.notification_progressbar, 100, handNum % 100, false);
        //设置事件
        Intent previous = new Intent("com.lx.myservicelx.MyReceiver");
        previous.putExtra("zt", "随机状态");
        PendingIntent pi_previous = PendingIntent.getBroadcast(activity, 0,
                previous, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_icon, pi_previous);
        //设置布局
        builder.setCustomContentView(remoteViews);   //折叠状态的样式
        builder.setCustomBigContentView(remoteViews);//展开状态的样式  可以不同一个布局
        //统一消除声音和震动
        builder.setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE);
        //自定义打开的界面
        Intent resultIntent = new Intent(activity, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(activity, 0,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        //管理和设置     不能关闭单独写没有用
        notificationManager = (NotificationManager) activity
                .getSystemService(NOTIFICATION_SERVICE);
        Notification build = builder.build();
        build.when = 0;
        //发送通知
        notificationManager.notify(12, build);
        if (util.isDestroy) {
            //关闭通知
            notificationManager.cancel(12);
        }
    }
}
