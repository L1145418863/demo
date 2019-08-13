package com.bossknow.android.san.music.bean;

/**
 * author : LiuZhenGuo Android Developer
 * create by : Administrator -- lx1
 * create date : 2019/7/26
 * create time : 10:42
 * function :
 */

/*
{
  "status": 0,
  "path": "https://XXXX.mp3",
  "musicName": "音乐名",
  "totalSecond": 0,
  "broadcastSecond": 0,
  "totalLength": 0,
  "broadcastTime": 0,
  "totalLengthSTR": "00:00",
  "broadcastTimeSTR": "00:00",
  "oneSecond": 1000,
  "speed": 1.2,
  "seekBarMAX": 1000,
  "autoplay": false
}
 */
public class ServiceAllStatus {

    /**
     * status : 0
     * path : https://XXXX.mp3
     * musicName : 音乐名
     * totalSecond : 0
     * broadcastSecond : 0
     * totalLength : 0
     * broadcastTime : 0
     * totalLengthSTR : 00:00
     * broadcastTimeSTR : 00:00
     * oneSecond : 1000
     * speed : 1
     * seekBarMAX : 1000
     * autoplay : false
     */

    private int status;
    private String path;
    private String musicName;
    private int totalSecond;
    private int broadcastSecond;
    private int totalLength;
    private int broadcastTime;
    private String totalLengthSTR;
    private String broadcastTimeSTR;
    private int oneSecond;
    private float speed;
    private int seekBarMAX;
    private boolean autoplay;

    public ServiceAllStatus() {

    }

    public ServiceAllStatus(int status, String path, String musicName, int totalSecond, int broadcastSecond, int totalLength, int broadcastTime, String totalLengthSTR, String broadcastTimeSTR, int oneSecond, float speed, int seekBarMAX, boolean autoplay) {
        this.status = status;
        this.path = path;
        this.musicName = musicName;
        this.totalSecond = totalSecond;
        this.broadcastSecond = broadcastSecond;
        this.totalLength = totalLength;
        this.broadcastTime = broadcastTime;
        this.totalLengthSTR = totalLengthSTR;
        this.broadcastTimeSTR = broadcastTimeSTR;
        this.oneSecond = oneSecond;
        this.speed = speed;
        this.seekBarMAX = seekBarMAX;
        this.autoplay = autoplay;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public int getTotalSecond() {
        return totalSecond;
    }

    public void setTotalSecond(int totalSecond) {
        this.totalSecond = totalSecond;
    }

    public int getBroadcastSecond() {
        return broadcastSecond;
    }

    public void setBroadcastSecond(int broadcastSecond) {
        this.broadcastSecond = broadcastSecond;
    }

    public int getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(int totalLength) {
        this.totalLength = totalLength;
    }

    public int getBroadcastTime() {
        return broadcastTime;
    }

    public void setBroadcastTime(int broadcastTime) {
        this.broadcastTime = broadcastTime;
    }

    public String getTotalLengthSTR() {
        return totalLengthSTR;
    }

    public void setTotalLengthSTR(String totalLengthSTR) {
        this.totalLengthSTR = totalLengthSTR;
    }

    public String getBroadcastTimeSTR() {
        return broadcastTimeSTR;
    }

    public void setBroadcastTimeSTR(String broadcastTimeSTR) {
        this.broadcastTimeSTR = broadcastTimeSTR;
    }

    public int getOneSecond() {
        return oneSecond;
    }

    public void setOneSecond(int oneSecond) {
        this.oneSecond = oneSecond;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getSeekBarMAX() {
        return seekBarMAX;
    }

    public void setSeekBarMAX(int seekBarMAX) {
        this.seekBarMAX = seekBarMAX;
    }

    public boolean isAutoplay() {
        return autoplay;
    }

    public void setAutoplay(boolean autoplay) {
        this.autoplay = autoplay;
    }

    @Override
    public String toString() {
        return "ServiceAllStatus{" +
                "\nstatus=" + status +
                ", \npath='" + path + '\'' +
                ", \nmusicName='" + musicName + '\'' +
                ", \ntotalSecond=" + totalSecond +
                ", \nbroadcastSecond=" + broadcastSecond +
                ", \ntotalLength=" + totalLength +
                ", \nbroadcastTime=" + broadcastTime +
                ", \ntotalLengthSTR='" + totalLengthSTR + '\'' +
                ", \nbroadcastTimeSTR='" + broadcastTimeSTR + '\'' +
                ", \noneSecond=" + oneSecond +
                ", \nspeed=" + speed +
                ", \nseekBarMAX=" + seekBarMAX +
                ", \nautoplay=" + autoplay +
                '}';
    }
}
