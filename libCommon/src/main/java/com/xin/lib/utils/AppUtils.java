package com.xin.lib.utils;


import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.xin.lib.log.Logger;


/**
 * <br> ClassName:    AppUtils
 * <br> Description:  全局Application（用于多模块Module访问）
 * <br>
 * <br> @author:      zouxinjie
 * <br> Date:         2019/8/17 11:52
 */
public class AppUtils {

    /**
     * 跨模块全局Application
     */
    public static Application INSTANCE;

    public static Application DEFAULT_APP;

    private static final Handler MAIN_HANDLER =
            new Handler(Looper.getMainLooper());

    /**
     * module 单独运行，为在application 中运行init()
     */
    static {
        Application app = null;
        try {
            app = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null);
            if (app == null) {
                throw new IllegalStateException("Static initialization of Applications must be on main thread.");
            }
        } catch (Exception e) {
            Logger.e("Failed to get current application from AppGlobals." + e.getMessage());
            try {
                app = (Application) Class.forName("android.app.ActivityThread")
                        .getMethod("currentApplication").invoke(null);
            } catch (Exception ex) {
                Logger.e("Failed to get current application from ActivityThread." + ex.getMessage());
            }
        } finally {
            INSTANCE = app;
        }
        if (INSTANCE == null) {
            INSTANCE = DEFAULT_APP;
        }
    }

    /**
     *<br> Description: App初始化（必须调用）
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:54
     */
    public static void init(Application application) {
        if (application == null) {
            throw new IllegalArgumentException("application must not be null.");
        }
        DEFAULT_APP = application;
        /*synchronized (AppUtils.class) {
            if (DEFAULT_APP != null) {
                throw new IllegalStateException("DEFAULT_APP already exists.");
            }
            DEFAULT_APP = application;
        }*/
    }

    public static Application getContext() {
        if (DEFAULT_APP != null) {
//            Logger.w("init should be called first!!!");
            return DEFAULT_APP;
        }
        return INSTANCE;
    }


    /**
     *<br> Description: 获取主线程Handler
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/21 11:23
     */
    public static Handler getDelivery() {
        return MAIN_HANDLER;
    }


    /**
     *<br> Description: 指定到UI线程执行
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/21 11:23
     */
    public static void runOnUIThread(Runnable runnable) {
        if (runnable != null) {
            MAIN_HANDLER.post(runnable);
        }
    }

    /**
     *<br> Description: 判断是否主线程
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/21 11:23
     */
    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

}
