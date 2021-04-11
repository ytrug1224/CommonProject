package com.xin.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.xin.lib.log.Logger;


public class ScreenUtils {
    private static final String TAG = "ScreenUtils";
    private static boolean isFullScreen = false;
    private static DisplayMetrics dm = null;

    public static DisplayMetrics displayMetrics(Context context) {
        if (null != dm) {
            return dm;
        }
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        Logger.d(TAG, "screen width=" + dm.widthPixels + "px, screen height=" + dm.heightPixels
                + "px, densityDpi=" + dm.densityDpi + ", density=" + dm.density);
        return dm;
    }

    public static int screenWidth(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int screenHeight(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F);
    }

    public static int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5F);
    }

    public static int dp2px(float dpValue) {
        float scale = AppUtils.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F);
    }

    public static int px2dp(float pxValue) {
        float scale = AppUtils.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5F);
    }

    /**
     *<br> Description: sp转px数值
     *<br> Author:      liaoshengjian
     *<br> Date:        2019/8/29 17:08
     */
    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }


    public static void setDefaultRootViewSize(@NonNull Context context, @NonNull ViewGroup rootView) {
        ViewGroup.LayoutParams rootParams = rootView.getLayoutParams();
        rootParams.width = -1;
        rootParams.height = dp2px(context, 45.0F);
        rootView.setLayoutParams(rootParams);
    }

    public static int getStatuWindowsHeight(@NonNull Context context) {
        return context.getResources().getDimensionPixelSize(context.getResources().getIdentifier("status_bar_height", "dimen", "android"));
    }
    /**
     * 判断导航栏是否显示
     *
     * @param context 上下文
     * @return 导航栏是否显示
     */
    public static boolean isShowDeviceHasNavigationBar(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager == null) {
                return false;
            }
            Display display = windowManager.getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            return !menu && !back;
        }
    }

    /**
     * 隐藏导航栏
     *
     * @param activity 显示界面
     */
    public static void hideTransparentNavigation(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 显示导航栏
     *
     * @param activity 显示界面
     */
    public static void showTransparentNavigation(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

    }

    /**
     * 判断导航栏高度
     *
     * @param context 上下文
     * @return 导航栏高度
     */
    public static int getNavigationHeight(@NonNull Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }


    /**
     *<br> Description: 获取手机屏幕密度api
     *<br> Author:      liaoshengjian
     *<br> Date:        2019/8/29 16:59
     */
    public static float getScreenDensity() {
        DisplayMetrics dm = AppUtils.getContext().getResources().getDisplayMetrics();
        return dm.density;
    }

    /**
     *<br> Description: 获取屏幕宽度
     *<br> Author:      liaoshengjian
     *<br> Date:        2019/8/29 16:59
     */
    public static int getScreenPixelsWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     *<br> Description: 获取屏幕高度
     *<br> Author:      liaoshengjian
     *<br> Date:        2019/8/29 16:59
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     *<br> Description: 获取屏幕分辨率
     *<br> Author:      liaoshengjian
     *<br> Date:        2019/8/29 16:59
     */
    public static String getResolution() {
        return getScreenPixelsWidth(AppUtils.getContext()) + "*" +
                getScreenHeight(AppUtils.getContext());
    }

    /**
     *<br> Description: 获取屏幕宽高
     *<br> Author:      liaoshengjian
     *<br> Date:        2019/8/29 16:59
     */
    public static int[] getDefaultDisplay(Context mContext) {
        int[] size = new int[]{0, 0};
        if (mContext != null) {
            WindowManager wm = (WindowManager) mContext
                    .getSystemService(Context.WINDOW_SERVICE);
            size = new int[]{wm.getDefaultDisplay().getWidth(), wm.getDefaultDisplay().getHeight()};
        }
        return size;
    }
}
