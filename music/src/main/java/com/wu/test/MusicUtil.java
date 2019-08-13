package com.wu.test;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/10/5.
 */
public class MusicUtil {
    private List<Music> musicList=new ArrayList<>();

    public static void getMusicList(Context context,List<Music> musicList) {
        musicList.clear();
        Cursor cursor=context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        BaseColumns._ID,             //歌曲id
                        MediaStore.Audio.AudioColumns.ARTIST,   //歌手
                        MediaStore.Audio.AudioColumns.DATA,           //文件路径
                        MediaStore.Audio.AudioColumns.DISPLAY_NAME,   //歌名
                        MediaStore.Audio.AudioColumns.SIZE,          //歌曲大小
                        MediaStore.Audio.AudioColumns.DURATION,      //歌曲持续时间
                        MediaStore.Audio.AudioColumns.ALBUM
                },
                null,
                null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        while(cursor.moveToNext()){
            long id=cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
            String name=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DISPLAY_NAME));
            String artist=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));
            String path=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
            int duration=cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION));
            long fileSize=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.SIZE));
            Music music=new Music();

                music.setMusic_name(formatMusic(name));
                music.setId(id);
                music.setArtist(formatArtist(name,artist));
                music.setDuration(duration);
                music.setFilesize(fileSize);
                music.setPath(path);
                // 分离出歌曲名和歌手
               if(music.getFilesize()>1000*800) {
                   musicList.add(music);
               }

        }
        cursor.close();
    }
    //格式化时间
    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return time / 1000 / 60 + ":0" + time / 1000 % 60;
        } else {
            return time / 1000 / 60 + ":" + time / 1000 % 60;
        }
    }

    //格式化歌曲名称
    private static String formatMusic(String name){
        if(name.contains("-")){
            String[] str= name.split("-") ;
            return str[1].replace(".mp3","");
        }
        return name.replace(".mp3","");
    }
    //获取出歌手名字
    private static String formatArtist(String name ,String aritist){
        if(name.contains("-")){
            String[] str= name.split("-") ;
            return str[0];
        }else {
            return aritist;
        }
    }

}
