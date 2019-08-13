package com.lx.myservicelx.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lx.myservicelx.util;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String zt = intent.getStringExtra("zt");
        Log.e("按钮",""+zt);
        util.okInterface.getStatus(1);
    }
}
