package com.bossknow.android.html;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * date:2019/4/17
 * author:刘振国(Administrator)
 * function:
 */
public class WebDataUtil {

    private final Context con;

    public WebDataUtil(Context context) {
        con = context;
    }

    public void setView(String Cs, int vipStatus) {
        boolean isNull = true;
        Document doc = Jsoup.parse(Cs);
        Elements ele = doc.getElementsByTag("p");
        for (Element elem : ele) {//遍历解析内容
            Document getBq = Jsoup.parse(elem + "");//获取具体内容
            Elements span = getBq.getElementsByTag("span");//Span标签内容
            Elements strong = getBq.getElementsByTag("strong");//加粗文字
            Elements em = getBq.getElementsByTag("em");//倾斜文字
            Elements a = getBq.getElementsByTag("a");//仅获取超链接的文字

            for (Element getSpan : span) {//Span标签内容
                isNull = false;
                TextView textView = new TextView(con);
                if (("" + getSpan).contains("font-weight:")) {//判断文字加粗
                    TextPaint paint = textView.getPaint();
                    paint.setFakeBoldText(true);
                }
                if (("" + getSpan).contains("font-size:")) {//改变文字大小
                    ChangeTextSize(getSpan, textView);
                }
                if (("" + getSpan).contains("color: rgb(")) {//改变文字颜色
                    ChangeTextColor(getSpan, textView);

                }
                textView.setLineSpacing(0, (float) 1.5);//设置字间距
                //设置上间距
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 8, 0, 0);
                textView.setLayoutParams(lp);
                //添加Html格式文字
                textView.setText(Html.fromHtml(getSpan + ""));
                //addView
                getHtmlView.getview(textView);
                break;
            }

            for (Element getStrong : strong) {//加粗文字
                isNull = false;
                TextView textView = new TextView(con);
                TextPaint paint = textView.getPaint();
                paint.setFakeBoldText(true);
                if (("" + getStrong).contains("font-size:")) {//改变文字大小
                    ChangeTextSize(getStrong, textView);

                }
                if (("" + getStrong).contains("color: rgb(")) {//改变文字颜色
                    ChangeTextColor(getStrong, textView);

                }
                textView.setLineSpacing(0, (float) 3);//设置字间距
                //设置上间距
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 8, 0, 0);
                textView.setLayoutParams(lp);
                //添加Html格式文字
                textView.setText(Html.fromHtml(getStrong + ""));
                //addView
                getHtmlView.getview(textView);
                break;
            }
            for (Element getEm : em) {//倾斜
                isNull = false;
                TextView textView = new TextView(con);
                if (("" + getEm).contains("font-size:")) {//改变文字大小

                    ChangeTextSize(getEm, textView);

                }
                if (("" + getEm).contains("color: rgb(")) {//改变文字颜色
                    ChangeTextColor(getEm, textView);

                }
                textView.setLineSpacing(0, (float) 1.5);//设置字间距
                //设置上间距
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 8, 0, 0);
                textView.setLayoutParams(lp);
                //添加Html格式文字
                textView.setText(Html.fromHtml(getEm + ""));
                //addView
                getHtmlView.getview(textView);
                break;
            }

            for (Element getA : a) {//超链接
                isNull = false;
                TextView textView = new TextView(con);
                if (("" + getA).contains("font-size:")) {//改变文字大小
                    ChangeTextSize(getA, textView);
                }
                if (("" + getA).contains("color: rgb(")) {//改变文字颜色
                    ChangeTextColor(getA, textView);
                }
                textView.setLineSpacing(0, (float) 1.5);//设置字间距
                //设置上间距
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 1, 0, 0);
                textView.setLayoutParams(lp);
                //添加Html格式文字
                textView.setText(Html.fromHtml(getA + ""));
                //addView
                getHtmlView.getview(textView);
                break;
            }

            Elements img = getBq.getElementsByTag("img");//图片
            for (Element getImg : img) {
                isNull = false;
                ImageView imageView = new ImageView(con);
                int index1 = ("" + getImg).indexOf("src=\"");
                String substring = ("" + getImg).substring(index1 + 5);
                int index2 = substring.indexOf("\"");
                String substring1 = substring.substring(0, index2);

                getHtmlView.getview(imageView);
            }

            if (isNull) { // 单纯使用P标签包的内容
                TextView textView = new TextView(con);
                textView.setText(elem + "");
                textView.setLineSpacing(0, (float) 1.5);//设置字间距
                //设置上间距
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 10, 0, 0);
                textView.setLayoutParams(lp);
                //添加Html格式文字
                textView.setText(Html.fromHtml(elem + ""));
                //addView
                getHtmlView.getview(textView);
                break;
            }
            isNull = true;
        }
    }

    //改变文字颜色
    private void ChangeTextColor(Element getStrong, TextView view) {
        int index1 = ("" + getStrong).indexOf("color: rgb(");
        String substring = ("" + getStrong).substring(index1 + 11);
        int index2 = substring.indexOf(");");
        String substring1 = substring.substring(0, index2);
        String[] rgb = substring1.split(",");
        if (rgb.length > 2) {
            view.setTextColor(Color.rgb(Integer.valueOf(rgb[0].trim()), Integer.valueOf(rgb[1].trim()), Integer.valueOf(rgb[2].trim())));
        }
    }

    //改变文字大小
    private void ChangeTextSize(Element getSpan, TextView textView) {
        int index1 = ("" + getSpan).indexOf("font-size:");//确定开始截取位置
        String substring = ("" + getSpan).substring(index1 + 10);//开始第一次截取
        int index2 = substring.indexOf("px;");
        if (index2 != -1) {
            String substring1 = substring.substring(0, index2);//确定截取内容
            if (Integer.valueOf(substring1.trim()) > 18) {
                textView.setTextSize(18);
            } else if (Integer.valueOf(substring1.trim()) < 12) {
                textView.setTextSize(10);
            }
        }
    }

    GetHtmlView getHtmlView;

    public void setGetHtmlView(GetHtmlView htmlView) {
        getHtmlView = htmlView;
    }

    public interface GetHtmlView {
        void getview(View view);
    }

}
