package com.bossknow.suspension;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * date:2019/4/9
 * author:刘振国(Administrator)
 * function: 热门视频
 */
public class RecycleViewHotAdapter extends RecyclerView.Adapter<RecycleViewHotAdapter.ViewHolders> {

    private final Context con;
    private final List<String> list;

    RecycleViewHotAdapter(Context context) {
        con = context;
        list = new ArrayList<>();
    }

    void setList(List<String> beans) {
        list.clear();
        if (beans != null) {
            list.addAll(beans);
        }
        notifyDataSetChanged();
    }

    public static class ViewHolders extends RecyclerView.ViewHolder {
        public TextView item_video_name;

        ViewHolders(@NonNull View itemView) {
            super(itemView);
            this.item_video_name = (TextView) itemView.findViewById(R.id.item_video_name);
        }
    }

    @NonNull
    @Override
    public ViewHolders onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video_n, viewGroup, false);
        View view = View.inflate(con, R.layout.item_video_n, null);
        return new ViewHolders(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolders viewHolders, int i) {
        viewHolders.item_video_name.setText(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
