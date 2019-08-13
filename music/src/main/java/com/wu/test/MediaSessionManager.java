package com.wu.test;

import android.content.Context;
import android.content.Intent;
import android.media.session.PlaybackState;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Clearlee on 2018/1/4 0004.
 */

public class MediaSessionManager {

    private static final String TAG = "MediaSessionManager";

    private MusicService musicPlayService;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder stateBuilder =null;
    private List<Music> musicList = new ArrayList<>();
    private Context context;

    public MediaSessionManager(MusicService service) {
        this.musicPlayService = service;
        initSession();
    }

    public void initSession() {
        try {
            mMediaSession = new MediaSessionCompat(musicPlayService, TAG);
            //指明支持的按键信息类型
            mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
            //监听的事件（播放，暂停，上一曲，下一曲）
            stateBuilder = new PlaybackStateCompat.Builder()
                    .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PLAY_PAUSE
                            | PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);

            mMediaSession.setPlaybackState(stateBuilder.build());
            //设置监听回调
            mMediaSession.setCallback(sessionCb);
            mMediaSession.setActive(true);
            MusicUtil.getMusicList(context,musicList);
        } catch (Exception e) {
        }
    }

    public void updatePlaybackState(boolean currentState) {
//        int state = (currentState == true) ? PlaybackStateCompat.STATE_PLAYING : PlaybackStateCompat.STATE_PAUSED ;
//        stateBuilder.setState(state, 1500, 1.0f);
//        mMediaSession.setPlaybackState(stateBuilder.build());
    }

    public void updateLocMsg(int position) {
        try {
            //同步歌曲信息
            MediaMetadataCompat.Builder md = new MediaMetadataCompat.Builder();
            md.putString(MediaMetadataCompat.METADATA_KEY_TITLE, musicList.get(position).getMusic_name());
            md.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, musicList.get(position).getArtist());
            //md.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, MusicUtil.getInstance().getCurrPlayMusicInfo().getAlbum());
            md.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, musicList.get(position).getDuration());
            mMediaSession.setMetadata(md.build());
        } catch (Exception e) {
        }

    }

    private MediaSessionCompat.Callback sessionCb = new MediaSessionCompat.Callback() {
        @Override
        public void onPlay() {
            super.onPlay();
        Intent intent1 = new Intent("PLAY");
        }

        @Override
        public void onPause() {
            super.onPause();
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
        }

    };

    public void release() {
        mMediaSession.setCallback(null);
        mMediaSession.setActive(false);
        mMediaSession.release();
    }

}
