package com.lx.myservicelx;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import static android.Manifest.permission.ACCESS_NOTIFICATION_POLICY;
import static android.Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.EXPAND_STATUS_BAR;
import static android.Manifest.permission.VIBRATE;

/**
 * date:2019/3/22
 * author:刘振国(Administrator)
 * function:
 * 权限检查工具
 */
public class PermissionsChecker {

    public static int PERMISSION_REQUEST_CODE = 500;
    Context context;
    public static String[] PERMISSIONS = {CALL_PHONE,EXPAND_STATUS_BAR,ACCESS_NOTIFICATION_POLICY,BIND_NOTIFICATION_LISTENER_SERVICE,VIBRATE};

    public PermissionsChecker(Context con) {
        context = con;
    }

    /**
     * 检查权限是否已请求到 (6.0)
     */
    public void checkPermissions(String... permissions) {
        // 版本兼容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                // 判断缺失哪些必要权限
                && lacksPermissions(permissions)) {
            // 如果缺失,则申请
            requestPermissions(permissions);
        }
    }

    /**
     * 判断是否缺失权限集合中的权限
     */
    public boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否缺少某个权限
     */
    public boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_DENIED;
    }

    /**
     * 请求权限
     */
    public void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions((Activity) context, permissions, PERMISSION_REQUEST_CODE);
    }

}