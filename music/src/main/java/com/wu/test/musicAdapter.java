package com.wu.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2018/10/5.
 */
public class musicAdapter extends BaseAdapter {
    private List<Music> musicList;
    private LayoutInflater inflater;



    public musicAdapter(List<Music> musicList, Context context){
         this.musicList=musicList;
         this.inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return musicList.size();
    }

    @Override
    public Music getItem(int position) {
        return musicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHoder viewHoder;
        Music music=getItem(position);
        if(convertView == null){
            viewHoder =new ViewHoder();
            view = inflater.inflate(R.layout.music_item,null);
            viewHoder.music_name= (TextView) view.findViewById(R.id.music_name);
            viewHoder.music_artist= (TextView) view.findViewById(R.id.music_artist);
            viewHoder.time = (TextView) view.findViewById(R.id.time);
            view.setTag(viewHoder);
        }else {
            view = convertView;
            viewHoder = (ViewHoder) view.getTag();
        }
        viewHoder.music_name.setText(music.getMusic_name().toString());
        viewHoder.music_artist.setText(music.getArtist().toString());
        viewHoder.time.setText(MusicUtil.formatTime(music.getDuration()));
        return view;
    }
    class ViewHoder{
        private TextView time;
        private TextView music_name;
        private  TextView music_artist;
    }
}
