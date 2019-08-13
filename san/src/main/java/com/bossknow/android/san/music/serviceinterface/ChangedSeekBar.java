package com.bossknow.android.san.music.serviceinterface;

/**
 * author : LiuZhenGuo Android Developer
 * create by : Administrator -- lx1
 * create date : 2019/7/25
 * create time : 18:06
 * function :
 */
public interface ChangedSeekBar {

    void seekBarMaxChanged(int seekMax, String totalLengthSTR, String musicName);

    void seekBarProgrossChanged(int progross, String broadcastTimeSTR);

    void seekBarsSecondaryProgressChanged(int secondaryProgress);
}
