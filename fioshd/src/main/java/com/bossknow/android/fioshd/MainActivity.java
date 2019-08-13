package com.bossknow.android.fioshd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aigestudio.wheelpicker.widgets.WheelAreaPicker;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private WheelAreaPicker select_one;
    private Button tan_k;
    //申明对象
    CityPickerView mPicker = new CityPickerView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        select_one = (WheelAreaPicker) findViewById(R.id.select_one);
        tan_k = (Button) findViewById(R.id.tan_k);
        //预先加载仿iOS滚轮实现的全部数据
        mPicker.init(this);
        tan_k.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tan_k:
                //添加默认的配置，不需要自己定义，当然也可以自定义相关熟悉，详细属性请看demo
                CityConfig cityConfig = new CityConfig.Builder().build();
                mPicker.setConfig(cityConfig);
                //监听选择点击事件及返回结果
                mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
                    @Override
                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                        //省份province
                        //城市city
                        //地区district
                        Toast.makeText(MainActivity.this,
                                "省份:" + province.toString()
                                        + "城市:" + province.toString()
                                        + "地区:" + province.toString()
                                , Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancel() {
                        Toast.makeText(MainActivity.this, "已取消", Toast.LENGTH_SHORT).show();
                    }
                });

                //显示
                mPicker.showCityPicker();
                break;
        }
    }

    public void Jump(View view) {

    }
}
