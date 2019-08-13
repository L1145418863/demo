package com.wq.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //构建通知
        Notification notification = new Notification();

        notification.icon = android.R.drawable.stat_notify_call_mute;
        notification.tickerText = "nice";
        //加载自定义布局
        RemoteViews contentView = new RemoteViews(getPackageName(),R.layout.weather_notification);
        //通知显示的布局
        notification.contentView = contentView;
        //设置值
        //remoteviews在RemoteViews这种调用方式中，你只能使用以下几种界面组件：
        // Layout:FrameLayout, LinearLayout, RelativeLayout Component:AnalogClock,
        // Button, Chronometer, ImageButton, ImageView, ProgressBar, TextView,
        // ViewFlipper, ListView, GridView, StackView, AdapterViewFlipper
        contentView.setImageViewResource(R.id.iv_notification_icon, R.drawable.qq);
        contentView.setTextViewText(R.id.tv_notification_times, "10次");
        contentView.setTextViewText(R.id.tv_notification_manys, "1个");
        contentView.setTextViewText(R.id.tv_notification_unlocks, "9次");

        //点击跳转
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 100, intent, 0);
        //点击的事件
        notification.contentIntent = contentIntent;
        //点击通知之后不消失
        notification.flags = Notification.FLAG_NO_CLEAR;
        //发送通知
        manager.notify(0, notification);


    }

}
