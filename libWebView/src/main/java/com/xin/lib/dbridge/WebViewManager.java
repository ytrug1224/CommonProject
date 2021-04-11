package com.xin.lib.dbridge;

import android.content.Context;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;


/**
 * ClassName:    WebViewManager
 * Description:  WebView 初始化内核环境，选择对应的WebView，初始化对应的内核
 *
 * @author: zouxinjie
 * Date:         2019/10/25 14:19
 */
public class WebViewManager {

    /**
     * 初始化X5内核，若已经初始化可忽略
     *
     * @param context
     */
    public static void initQbSdk(final Context context) {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {
                //x5内核初始化完成回调接口，此接口回调并表示已经加载起来了x5，有可能特殊情况下x5内核加载失败，切换到系统内核
                Log.d("tbs", "加载内核是否成功:" + b);
                if (!b) {
                    initQbSdk(context.getApplicationContext());
                }
            }
        };
        QbSdk.initX5Environment(context.getApplicationContext(), cb);
    }
}
