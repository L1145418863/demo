package com.bossknow.android.san.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bossknow.android.san.R;
import com.bossknow.android.san.music.bean.ServiceAllStatus;
import com.bossknow.android.san.music.serviceinterface.ChangedSeekBar;
import com.bossknow.android.san.music.serviceinterface.OnMediaPlayerStatus;
import com.bossknow.android.san.music.util.CircularProgressBar;
import com.bossknow.android.san.music.util.MusicManage;

import static com.bossknow.android.san.music.service.MusicService.MEDIA_STATUS_ALREADY;
import static com.bossknow.android.san.music.service.MusicService.MEDIA_STATUS_DO_PAUSE;
import static com.bossknow.android.san.music.service.MusicService.MEDIA_STATUS_IS_COMPLETE;
import static com.bossknow.android.san.music.service.MusicService.MEDIA_STATUS_IS_ERROR;
import static com.bossknow.android.san.music.service.MusicService.MEDIA_STATUS_IS_PAUSE;
import static com.bossknow.android.san.music.service.MusicService.MEDIA_STATUS_IS_TSTART;
import static com.bossknow.android.san.music.service.MusicService.MEDIA_STATUS_LOADER;
import static com.bossknow.android.san.music.service.MusicService.MEDIA_STATUS_NOTINIT;
import static com.bossknow.android.san.music.service.MusicService.MEDIA_STATUS_NOTSTART;
import static com.bossknow.android.san.music.service.MusicService.MEDIA_STATUS_URL_NULL;

public class Main2Activity extends AppCompatActivity implements ChangedSeekBar, OnMediaPlayerStatus {

    private ImageView mMusicImage;
    private SeekBar mMusicSeekbar;
    private TextView mMusicStart;
    private TextView mMusicPrevious;
    private TextView mMusicNext;
    private TextView mMusicSent;
    private TextView mMusicTotalLength;
    private TextView mMusicName;
    private TextView mServiceMessage;
    private RelativeLayout include;
    private boolean serviceIsBinder;
    private StringBuffer stringBuffer;
    private CircularProgressBar circular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        stringBuffer = new StringBuffer();
        initView();
        initData();
        initListener();
    }

    private void initView() {
        mMusicImage = (ImageView) findViewById(R.id.music_image);
        mMusicSeekbar = (SeekBar) findViewById(R.id.music_seekbar);
        mMusicStart = (TextView) findViewById(R.id.music_start);
        mMusicPrevious = (TextView) findViewById(R.id.music_previous);
        mMusicNext = (TextView) findViewById(R.id.music_next);
        mMusicSent = (TextView) findViewById(R.id.music_sent);
        mMusicTotalLength = (TextView) findViewById(R.id.music_total_length);
        mMusicName = (TextView) findViewById(R.id.music_name);
        mServiceMessage = (TextView) findViewById(R.id.service_message);

        include = (RelativeLayout) findViewById(R.id.include);
        circular = include.findViewById(R.id.floating_circular);
        circular.setCircleWidth(5);
        circular.setBackgroundColor(Color.rgb(255, 255, 255));
        circular.setPrimaryColor(Color.rgb(0, 255, 255));
        circular.setProgress(0);
    }

    private void initData() {
        serviceIsBinder = MusicManage.musicService != null;
        stringBuffer.append(MusicManage.musicService.getAllStatus().toString() + "\n");
        stringBuffer.append("serviceIsBinder状态" + serviceIsBinder + "\n");
        mServiceMessage.setText(stringBuffer.toString());
        if (serviceIsBinder) {
            MusicManage.musicService.setUp(this, "https://file.kuyinyun.com/group1/M00/90/B7/rBBGdFPXJNeAM-nhABeMElAM6bY151.mp3", "当你老了");
            MusicManage.musicService.setChangedSeekBar(this);
            MusicManage.musicService.setOnMediaPlayerStatus(this);

            ServiceAllStatus allStatus = MusicManage.musicService.getAllStatus();
            mMusicSeekbar.setMax(allStatus.getSeekBarMAX());
            circular.setMax(allStatus.getSeekBarMAX());
            mMusicSeekbar.setProgress(allStatus.getBroadcastSecond());
            circular.setProgress(allStatus.getBroadcastSecond());
            mMusicTotalLength.setText(allStatus.getTotalLengthSTR());
            mMusicSent.setText(allStatus.getBroadcastTimeSTR());
            mMusicName.setText(allStatus.getMusicName());
            int status = allStatus.getStatus();
            PlayBtnUI(status);
        }
    }

    private void PlayBtnUI(int status) {
        switch (status) {
            case MEDIA_STATUS_NOTINIT://未初始化
                mMusicStart.setText("I");
                break;
            case MEDIA_STATUS_ALREADY://准备好了可以播放
                mMusicStart.setText("R");
                break;
            case MEDIA_STATUS_LOADER://正在加载
                mMusicStart.setText("L");
                break;
            case MEDIA_STATUS_URL_NULL://播放地址为null
                mMusicStart.setText("*");
                break;
            case MEDIA_STATUS_NOTSTART://尚未开始 可以开始
                mMusicStart.setText("开");
                break;
            case MEDIA_STATUS_IS_TSTART://正在播放
                mMusicStart.setText("停");
                break;
            case MEDIA_STATUS_DO_PAUSE://即将暂停
                mMusicStart.setText("即");
                break;
            case MEDIA_STATUS_IS_PAUSE://暂停状态
                mMusicStart.setText("开");
                break;
            case MEDIA_STATUS_IS_COMPLETE://播放完成
                mMusicStart.setText("完");
                break;
            case MEDIA_STATUS_IS_ERROR://播放出错
                mMusicStart.setText("错");
                break;
            default://错误状态
                mMusicStart.setText("X");
                break;
        }
    }

    private void initListener() {
        mMusicStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serviceIsBinder) {
                    MusicManage.musicService.MediaPlayOrPause();
                }
            }
        });
        mMusicSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                MusicManage.musicService.SeekTo(progress);
            }
        });
    }

    @Override
    public void seekBarMaxChanged(int seekMax, String totalLengthSTR, String musicName) {
        mMusicSeekbar.setMax(seekMax);
        mMusicTotalLength.setText(totalLengthSTR);
        mMusicName.setText(musicName);
        stringBuffer.append("时长" + totalLengthSTR + "-名字:" + musicName + "\n");
        mServiceMessage.setText(stringBuffer.toString());
        circular.setMax(seekMax);
    }

    @Override
    public void seekBarProgrossChanged(int progross, String broadcastTimeSTR) {
        mMusicSeekbar.setProgress(progross);
        mMusicSent.setText(broadcastTimeSTR);
        circular.setProgress(progross);
    }

    @Override
    public void seekBarsSecondaryProgressChanged(int secondaryProgress) {
        mMusicSeekbar.setSecondaryProgress(secondaryProgress);
    }

    @Override
    public void mediaPlayerStatus(int status) {
        stringBuffer.append("状态" + status + "\n");
        mServiceMessage.setText(stringBuffer.toString());
        PlayBtnUI(status);
    }

    public void FinishMain2(View view){
        finish();
    }

}
