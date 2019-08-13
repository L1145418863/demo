package com.bossknow.android.webviewdemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button web_bd;
    private Button web_bing;
    private Button web_sg;
    private WebView web_view;
    private WebSettings webSettings;
    private String baidu = "https://www.baidu.com/";
    private String bing = "https://cn.bing.com/";
    private String sougou = "https://www.sogou.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();


        web_view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                //view.getViewTreeObserver().removeOnPreDrawListener(this);
                Log.e("View变化", "\nHeight:" + web_view.getHeight() + "\nWidth:" + web_view.getWidth());
                return true;
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        web_bd = (Button) findViewById(R.id.web_bd);
        web_bing = (Button) findViewById(R.id.web_bing);
        web_sg = (Button) findViewById(R.id.web_sg);
        web_view = (WebView) findViewById(R.id.web_view);

        web_bd.setOnClickListener(this);
        web_bing.setOnClickListener(this);
        web_sg.setOnClickListener(this);

        webSettings = web_view.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //缩放操作
        webSettings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.web_bd:
                web_view.loadUrl(baidu);
                break;
            case R.id.web_bing:
                web_view.loadUrl(bing);
                break;
            case R.id.web_sg:
                web_view.loadUrl(sougou);
                break;
        }
    }
}
