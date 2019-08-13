package com.bossknow.android.html;

import android.widget.ImageView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * author : LiuZhenGuo Android Developer
 * create by : Administrator -- lx1
 * create date : 2019/8/8
 * create time : 17:00
 * function :
 */
public class HtmlUtil {
    private static List<String> list = new ArrayList();

    public static List<String> getImageUrl(String date) {
        Document parse = Jsoup.parse(date);
        Elements ele = parse.getElementsByTag("p");
        for (Element elem : ele){
            Elements img = elem.getElementsByTag("img");//图片
            for (Element getImg : img) {
                int index1 = ("" + getImg).indexOf("src=\"");
                String substring = ("" + getImg).substring(index1 + 5);
                int index2 = substring.indexOf("\"");
                String substring1 = substring.substring(0, index2);
                list.add(substring1);
            }
        }
        return list;
    }
}
