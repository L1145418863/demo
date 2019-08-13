package com.bossknow.suspension;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private AppBarLayout appbarLayout;
    private RecyclerView viewpager;
    private CoordinatorLayout mainContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        appbarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        viewpager = (RecyclerView) findViewById(R.id.viewpager);
        mainContent = (CoordinatorLayout) findViewById(R.id.main_content);

        RecycleViewHotAdapter recycleViewHotAdapter = new RecycleViewHotAdapter(this);
        viewpager.setAdapter(recycleViewHotAdapter);
        viewpager.setLayoutManager(new LinearLayoutManager(this));
        List<String> list = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            list.add("<--"+i+"-->");
        }
        recycleViewHotAdapter.setList(list);
    }
}
