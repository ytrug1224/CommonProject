package com.xin.lib.log;

import android.text.TextUtils;

/**
 * <br> ClassName:    Logger
 * <br> Description:  日志管理类
 * <br>
 * <br> @author:      zouxinjie
 * <br> Date:         2019/8/17 11:27
 */
public class Logger {

    private static String TAG = "Logger";
    private static ILogger logger;

    /**
     *<br> Description: 设置默认TAG
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:38
     */
    public static void setTag(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            TAG = tag;
        }
    }

    /**
     *<br> Description: 设置ILogger
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:38
     */
    public static void setLogger(ILogger newLogger) {
        if (newLogger == null) {
            throw new NullPointerException("newLogger == null");
        }
        logger = newLogger;
    }

    /**
     *<br> Description: 打印详细信息，默认Tag
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:38
     */
    public static void v(String msg) {
        if (logger != null) {
            logger.v(TAG, msg);
        }
    }

    /**
     *<br> Description: 打印详细信息，自定义Tag
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:38
     */
    public static void v(String tag, String msg) {
        if (logger != null) {
            logger.v(tag, msg);
        }
    }

    /**
     *<br> Description: 打印详细信息，自定义Tag方法，还有异常信息
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:38
     */
    public static void v(String tag, String msg, Throwable tr) {
        if (logger != null) {
            logger.v(tag, msg, tr);
        }
    }

    /**
     *<br> Description: 打印debug信息，默认Tag
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:38
     */
    public static void d(String msg) {
        if (logger != null) {
            logger.d(TAG, msg);
        }
    }

    /**
     *<br> Description: 打印debug信息，自定义Tag
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:38
     */
    public static void d(String tag, String msg) {
        if (logger != null) {
            logger.d(tag, msg);
        }
    }

    /**
     *<br> Description: 打印debug信息，自定义Tag方法，还有异常信息
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:38
     */
    public static void d(String tag, String msg, Throwable tr) {
        if (logger != null) {
            logger.d(tag, msg, tr);
        }
    }

    /**
     *<br> Description: 打印信息，默认Tag
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:38
     */
    public static void i(String msg) {
        if (logger != null) {
            logger.i(TAG, msg);
        }
    }

    /**
     *<br> Description: 打印信息，自定义Tag
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:38
     */
    public static void i(String tag, String msg) {
        if (logger != null) {
            logger.i(tag, msg);
        }
    }

    /**
     *<br> Description: 打印信息，自定义Tag方法，还有异常信息
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:39
     */
    public static void i(String tag, String msg, Throwable tr) {
        if (logger != null) {
            logger.i(tag, msg, tr);
        }
    }

    /**
     *<br> Description: 打印警告信息，默认Tag
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:39
     */
    public static void w(String msg) {
        if (logger != null) {
            logger.w(TAG, msg);
        }
    }

    /**
     *<br> Description: 打印警告信息，自定义Tag
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:39
     */
    public static void w(String tag, String msg) {
        if (logger != null) {
            logger.w(tag, msg);
        }
    }

    /**
     *<br> Description: 打印警告信息，自定义Tag方法，还有异常信息
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:39
     */
    public static void w(String tag, String msg, Throwable tr) {
        if (logger != null) {
            logger.w(tag, msg, tr);
        }
    }

    /**
     *<br> Description: 打印错误信息，默认Tag
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:39
     */
    public static void e(String msg) {
        if (logger != null) {
            logger.e(TAG, msg);
        }
    }

    /**
     *<br> Description: 打印错误信息，自定义Tag
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:39
     */
    public static void e(String tag, String msg) {
        if (logger != null) {
            logger.e(tag, msg);
        }
    }

    /**
     *<br> Description: 打印错误信息，自定义Tag方法，还有异常信息
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:39
     */
    public static void e(String tag, String msg, Throwable tr) {
        if (logger != null) {
            logger.e(tag, msg, tr);
        }
    }

    /**
     *<br> Description: JSON格式，打印信息，默认tag
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:39
     */
    public static void json(String msg) {
        if (logger != null) {
            logger.json(TAG, msg);
        }
    }

    /**
     *<br> Description: JSON格式，打印信息，默认tag
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:39
     */
    public static void json(String tag, String msg) {
        if (logger != null) {
            logger.json(tag, msg);
        }
    }


}
