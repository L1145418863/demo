package com.onlyboss.lx1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    private Button huoqu;
    private TextView num;
    private LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
    }

    private void initView() {
        huoqu = (Button) findViewById(R.id.huoqu);
        num = (TextView) findViewById(R.id.num);
        linear = (LinearLayout) findViewById(R.id.linear);

        huoqu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.huoqu:
                int length = Cheeses.NAMESS.length;
                num.setText("数量:" + length);
                for (int i = 0; i < length; i++) {
                    TextView textView = new TextView(this);
                    textView.setText("银行:--->"+Cheeses.NAMESS[i]);
                    linear.addView(textView);
                }
                /*int length = Cheeses.NAMESS.length;
                Log.e("银行"," "+length);
                //填充
                for(int i = 0; i<length; i++){
                    Log.e("银行名字",""+Cheeses.NAMESS[i]+" "+Cheeses.NAMESS.length);
                }*/
                break;
        }
    }
}
