package com.bossknow.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView t8;
    private EditText ed1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initView() {
        t8 = (TextView) findViewById(R.id.t8);
        ed1 = (EditText) findViewById(R.id.ed1);
    }

    private void initListener() {
        ed1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                t8.setText(s+"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
