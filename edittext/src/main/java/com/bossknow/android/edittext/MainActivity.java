package com.bossknow.android.edittext;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fynn.fluidlayout.FluidLayout;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private EditText input;
    private LinearLayout addTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        myFluidLayout();
    }

    private void initView() {
        input = (EditText) findViewById(R.id.input);
        addTextview = (LinearLayout) findViewById(R.id.addTextview);
        addTextview.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        addTextview.getViewTreeObserver().removeOnPreDrawListener(this);
                        int height = addTextview.getHeight();// 获取高度
                        for (int i = 0; i < 4; i++) {
                            TextView child = (TextView) View.inflate(MainActivity.this, R.layout.extview, null);
                            addTextview.addView(child);
                            LinearLayout.LayoutParams Params = (LinearLayout.LayoutParams) child.getLayoutParams();
                            Params.height = height - 30;
                            Params.width = height - 30;
                            Params.topMargin = 15;
                            Params.leftMargin = 15;
                            Params.rightMargin = 15;
                            Params.bottomMargin = 15;
                            if (i == 0) {
                                child.setBackgroundResource(R.drawable.inputshape2);
                            } else {
                                child.setBackgroundResource(R.drawable.inputshape);
                            }
                            child.setLayoutParams(Params);
                        }
                        return true;
                    }
                });


    }

    private void initListener() {
        input.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for (int i = 0; i < 4; i++) {
                    TextView childAt = (TextView) addTextview.getChildAt(i);
                    if (s.length() - 1 == i) {
                        childAt.setText("" + s.charAt(i));
                    } else if (i > s.length() - 1) {
                        childAt.setText("");
                    }
                    if (s.length() == i) {
                        childAt.setBackgroundResource(R.drawable.inputshape2);
                    } else {
                        childAt.setBackgroundResource(R.drawable.inputshape);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void ChangColor(int index) {

    }


    private void myFluidLayout() {
        FluidLayout myfluid = findViewById(R.id.myfluid);
        myfluid.removeAllViews();
        myfluid.setGravity(Gravity.TOP);
        FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        for (int i = 0; i < 30; i++) {
            params.setMargins(DpPxUtil.dip2px(this, 6), DpPxUtil.dip2px(this, 6), DpPxUtil.dip2px(this, 7), DpPxUtil.dip2px(this, 7));
            final TextView textView = (TextView) View.inflate(this, R.layout.my_text, null);
            textView.setText(""+ (i * i * i));
            myfluid.addView(textView, params);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, textView.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
