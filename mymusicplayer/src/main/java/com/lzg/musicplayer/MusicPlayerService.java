package com.lzg.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MusicPlayerService extends Service {
    public String TAG = "MusicPlayerService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "-------------create-------------");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "-------------onBind-------------");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "-------------onStartCommand-------------");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "-------------onDestroy-------------");
        super.onDestroy();
    }
}
