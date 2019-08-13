package com.wu.test;

/**
 * Created by Administrator on 2018/10/5.
 */
public class Music {
    private long id;          //音乐id
    private String music_name;  //音乐名
    private String artist;   //音乐歌手
    private String path;    //音乐地址
    private int duration;  //音乐的时间
    private long filesize;   //音乐大小

    public void setId(long id) {
        this.id = id;
    }

    public void setMusic_name(String music_name) {
        this.music_name = music_name;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public long getId() {
        return id;
    }

    public String getMusic_name() {
        return music_name;
    }

    public String getArtist() {
        return artist;
    }

    public String getPath() {
        return path;
    }

    public int getDuration() {
        return duration;
    }

    public long getFilesize() {
        return filesize;
    }
}
