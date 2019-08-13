package com.bossknow.android.san.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.bossknow.android.san.R;
import com.bossknow.android.san.music.service.MusicService;
import com.bossknow.android.san.music.service.KeepLifeService;
import com.bossknow.android.san.music.util.MusicManage;

public class MainActivity extends AppCompatActivity {

    private TextView jump;
    private MemoryBoss mMemoryBoss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mMemoryBoss = new MemoryBoss();
            registerComponentCallbacks(mMemoryBoss);
        }
        jump = findViewById(R.id.btn_jump);
        jump.setVisibility(View.GONE);
        MusicManage.activity = this;
        startService(new Intent(this, KeepLifeService.class));
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
                //ServiceUtil.musicService.setUp("https://file.kuyinyun.com/group1/M00/90/B7/rBBGdFPXJNeAM-nhABeMElAM6bY151.mp3");
            }
        });
    }

    public void BinderService(View view) {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, sconn, BIND_AUTO_CREATE);
    }

    ServiceConnection sconn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicManage.musicService = ((MusicService.MusicServiceBinder) service).getMusicService();
            jump.setVisibility(View.VISIBLE);
            MusicManage.sconn = sconn;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public void closeActivity(View v) {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            Log.e("app进程", "回到桌面");
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        try {
            unbindService(sconn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            unregisterComponentCallbacks(mMemoryBoss);
        }
        super.onDestroy();
    }


}
