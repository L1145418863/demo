package com.bossknow.android.san.music.util;

/**
 * author : LiuZhenGuo Android Developer
 * create by : Administrator -- lx1
 * create date : 2019/7/25
 * create time : 18:16
 * function :
 */
public class TimeUtil {
    public static String getTime(int durations) {
        int temp;
        StringBuilder sb = new StringBuilder();
        temp = durations / 3600;
        if (temp != 0) {//时长不超过一小时则不添加
            sb.append((temp < 10) ? "0" + temp + ":" : "" + temp + ":");
        }
        temp = durations % 3600 / 60;
        sb.append((temp < 10) ? "0" + temp + ":" : "" + temp + ":");
        temp = durations % 3600 % 60;
        sb.append((temp < 10) ? "0" + temp : "" + temp);
        return sb.toString();
    }
}
