package com.onlyboss.lx1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.lzg.musicplayer.MyMusicPlayerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        MyMusicPlayerView my_music_view = findViewById(R.id.my_music_view);
        ImageView my_image = findViewById(R.id.my_image);
        my_image.setVisibility(View.VISIBLE);
        String url = "http://file.kuyinyun.com/group1/M00/90/B7/rBBGdFPXJNeAM-nhABeMElAM6bY151.mp3";
        String image = "https://cn.bing.com/th?id=OHR.Narrenmuehle_ZH-CN5582540867_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp";
        String image2 = "https://cn.bing.com/th?id=OHR.LeatherbackTT_ZH-CN5495532728_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp";
        my_music_view.setUp(url, image2);
    }
}
