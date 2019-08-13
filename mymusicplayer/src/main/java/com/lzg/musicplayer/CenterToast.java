package com.lzg.musicplayer;

import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CenterToast {

    private final Context context;
    private Toast baseToast;

    public CenterToast(Context context, String toastText, View title, LinearLayout linearLayout){
        this.context = context;
        int height = title.getHeight() / 2;
        int toastHeight = linearLayout.getHeight() / 2;
        if (baseToast == null) {
            baseToast = Toast.makeText(context, toastText, Toast.LENGTH_LONG);
            baseToast.setGravity(Gravity.TOP, 0, height - toastHeight);
            View view = LayoutInflater.from(context).inflate(R.layout.jz_layout_toastlayout, null, false);
            TextView tv = (TextView) view.findViewById(R.id.toast_text);
            tv.setText(Html.fromHtml(toastText));
            baseToast.setView(view);
        } else {
            baseToast.setGravity(Gravity.TOP, 0, height - toastHeight);
            View view = LayoutInflater.from(context).inflate(R.layout.jz_layout_toastlayout, null, false);
            TextView tv = (TextView) view.findViewById(R.id.toast_text);
            tv.setText(Html.fromHtml(toastText));
            baseToast.setView(view);
        }
        baseToast.show();
    }
}
