package com.wu.test;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

/**
 * Created by Administrator on 2018/10/4.
 */
public class MyActionProvider extends ActionProvider {
    /**
     * Creates a new instance.
     *
     * @param context Context for accessing resources.
     */
    public MyActionProvider(Context context) {
        super(context);
    }

    @Override
    public View onCreateActionView() {
        return null;
    }

    @Override
    public boolean hasSubMenu() {
        return true;
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {
        subMenu.clear();
        subMenu.add("发起群聊").setIcon(R.drawable.peples).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        subMenu.add("添加朋友").setIcon(R.drawable.add).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        subMenu.add("扫一扫").setIcon(R.drawable.saoyisao).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        subMenu.add("收付款").setIcon(R.drawable.money).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        subMenu.add("帮助和反馈").setIcon(R.drawable.help ).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
    }
}
