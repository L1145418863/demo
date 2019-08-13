package com.bossknow.android.san.activity;

import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.util.Log;

/**
 * author : LiuZhenGuo Android Developer
 * create by : Administrator -- lx1
 * create date : 2019/8/6
 * create time : 14:17
 * function :
 */
public class MemoryBoss implements ComponentCallbacks2 {
    @Override
    public void onTrimMemory(int level) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            // 进入后台
            Log.e("监听App状态","进入后台");
        }
        Log.e("监听App状态","其他状态"+level);
        // 如果有必要，你也可以进行一些清理内存操作
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onLowMemory() {

    }
}
