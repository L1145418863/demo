package com.lx.myservicelx;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.lx.myservicelx.service.MyService;

import static android.Manifest.permission.ACCESS_NOTIFICATION_POLICY;

public class MainActivity extends AppCompatActivity {

    private MyService myservice;
    private Intent intent1;
    private EditText edit;
    private RemoteViews remoteViews;
    private NotificationCompat.Builder builder;
    String hand = "activity进度";
    int handNum = 1;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            handler.sendEmptyMessageDelayed(1000, 1000);
            Log.e(hand, handNum + "S");
            edit.setText(handNum + "S");
            MyTong();
            handNum++;
            return false;
        }
    });
    private PermissionsChecker checker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edit = findViewById(R.id.edit_text);

    }

    public void btClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btbind:
                myservice.setData("穿进去了么");
                break;
            case R.id.btclick:
                Toast.makeText(this, myservice.getData(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_bind:
                Intent intent = new Intent(this, MyService.class);
                bindService(intent, conn, Context.BIND_AUTO_CREATE);
                Toast.makeText(this, "绑定Service", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_start:
                XiTong();
                break;
            case R.id.bt_my:
                handler.sendEmptyMessageDelayed(1, 0);
                //MyTong();
                break;
            case R.id.bt_service_tz:
                myservice.MyTongZhi(this);
                break;
        }
    }

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myservice = ((MyService.MyServicMyServiceeBinder) service).getMyservice();
            myservice.setMyServiceInterface(new MyService.MyServiceInterface() {
                @Override
                public void ServiceGetDate(boolean yesOrNo, String str) {
                    Toast.makeText(MainActivity.this, yesOrNo + "---" + str, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unbindService(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            stopService(intent1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void MyTong() {
        builder = new NotificationCompat.Builder(this,util.createNotificationChannel(this));
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);
        builder.setOnlyAlertOnce(true);
        // 需要VIBRATE权限
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setPriority(Notification.PRIORITY_DEFAULT);
        remoteViews = new RemoteViews("com.lx.myservicelx", R.layout.notification_layout);
        remoteViews.setTextViewText(R.id.notification_progress, edit.getText());
        builder.setCustomContentView(remoteViews);
        // Creates an explicit intent for an Activity in your app
        //自定义打开的界面
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    private void XiTong() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,util.createNotificationChannel(this));
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("系统通知");
        builder.setContentText(edit.getText().toString());
        builder.setAutoCancel(true);
        builder.setOnlyAlertOnce(true);
        // 需要VIBRATE权限
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setPriority(Notification.PRIORITY_DEFAULT);

        // Creates an explicit intent for an Activity in your app
        //自定义打开的界面
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(R.id.notification_progress, builder.build());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
