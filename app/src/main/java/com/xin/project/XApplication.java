package com.xin.project;

import android.app.Application;

import androidx.multidex.BuildConfig;

import com.alibaba.android.arouter.launcher.ARouter;

public class XApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initARouter(this);
//        WebViewManager.initQbSdk(this);
    }

    /**
     *<br> Description: 初始化ARouter
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/27 15:46
     */
    private void initARouter(Application application) {
        ARouter.init(application);
        if(BuildConfig.DEBUG) {
            ARouter.openDebug();
        }
    }
}
