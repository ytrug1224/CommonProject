package com.xin.lib.utils;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * <br> ClassName:    NetworkUtils
 * <br> Description:  网络工具类
 * <br>
 * <br> @author:      zouxinjie
 * <br> Date:         2019/8/29 16:18
 */
public class NetworkUtils {

    /**
     *<br> Description: 是否有网络
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/29 16:18
     */
    public static boolean isNetWorkAvailable(Application mContext) {
        if (mContext == null) {
            throw new IllegalArgumentException("mContext must not be null !!!");
        }
        ConnectivityManager mgr = (ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mgr != null) {
            NetworkInfo info = mgr.getActiveNetworkInfo();
            if (info != null && info.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }


    /**
     *<br> Description: 当前是否Wifi网络
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/29 16:18
     */
    public static boolean isWifiNet(Application mContext) {
        if (mContext == null) {
            throw new IllegalArgumentException("mContext must not be null !!!");
        }

        ConnectivityManager mgr = (ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mgr != null) {
            NetworkInfo info = mgr.getActiveNetworkInfo();
            if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

}
