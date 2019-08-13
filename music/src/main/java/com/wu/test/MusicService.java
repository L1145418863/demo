package com.wu.test;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicService extends Service {
    private final static String TAG = "MusicService";
    public MediaPlayer mPlayer;
    private List<Music> musicList =new ArrayList<>();
    private int currentposition =0;
    public int type = 1,flag;



    class MyBinder extends Binder{

//        //播放音乐
//        public void play(String path) throws IOException {
//            Log.i(TAG,"开始播放");
//            MusicService.this.play(path);
//        }
//        //暂停播放
//        public void pause(){
//            Log.i(TAG,"暂停播放");
//            MusicService.this.pause();
//        }
//        //重新播放
//        public void replay(String path) throws IOException {
//            Log.i(TAG,"重新播放");
//            MusicService.this.replay(path);
//        }
//        //停止播放
//        public void stop(){
//            Log.i(TAG,"停止播放");
//            MusicService.this.stop();
//        }
//        //获取当前播放进度
//        public int getCurrentPosition(){
//            return getCurrentProgress();
//        }
//        //获取文件大小
//        public int getMusicSize(){
//            return getMusicLength();
//        }
       public MusicService getService(){
           return MusicService.this;
       }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
       super.onCreate();
    }
    //播放音乐
    public void play(String path) throws IOException {
        if(mPlayer == null){
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(path);
            mPlayer.prepare();
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mPlayer.start();
                }
            });

        }else {
            mPlayer.reset();
//            int position = getCurrentProgress();
//            mPlayer.seekTo(position);
            mPlayer.setDataSource(path);
            mPlayer.prepare();
            mPlayer.start();
        }
    }
    //判断是否播放正在歌曲
    public boolean isPlaying(){
        if(mPlayer!=null ){
            return mPlayer.isPlaying();
        }
        return false;
    }


    //获取当前播放进度
    public int getCurrentProgress() {
        if(mPlayer !=null&&mPlayer.isPlaying()){
            return mPlayer.getCurrentPosition();
        }else if(mPlayer !=null && (!mPlayer.isPlaying())){
            return mPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void setCurrentProgress(int progross){
        if(mPlayer != null ){
            mPlayer.seekTo(progross);
        }
    }
    //暂停播放
    public void playORpause() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        } else if (mPlayer != null && (!mPlayer.isPlaying())) {
            mPlayer.start();
        }
    }
//    //开始播放
//    public void start(){
//        mPlayer.start();
//    }
    //停止播放
    public void stop(){
        if(mPlayer !=null){
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }else {
            Log.i(TAG,"已经停止播放");
        }
    }
    //重新播放
    public void replay(String path) throws IOException {
        if(mPlayer !=null){
            mPlayer.seekTo(0);
            mPlayer.prepare();
            mPlayer.start();
        }
    }
    //获取资源文件的长度
    public int getMusicLength(){
        if(mPlayer !=null){
            return mPlayer.getDuration();
        }
        return 0;
    }
    //播放下一首
    public void playNext(int position) throws IOException {
        position++;
        if(position > musicList.size()-1){
            position = 0;
        }
        play(musicList.get(position).getPath());
    }

    //播放上一首
    public void playPre(int position) throws IOException {
        position--;
        if(position < 0){
            position = musicList.size()-1;
        }
        play(musicList.get(position).getPath());
    }
    public void playMusic(int position) throws IOException {
        play(musicList.get(position).getPath());
    }
    @Override
    public void onDestroy() {
        if(mPlayer !=null){
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        super.onDestroy();
    }
    public void getType(){
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Intent intent1 = new Intent("wu.com.PlayType");//发送广播给活动提醒歌曲播放完毕
                sendBroadcast(intent1);
                Log.i(TAG,"广播发送出去了");
            }
        });
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.i(TAG,"错误码:"+what);
                return true;
            }
        });
    }
}
