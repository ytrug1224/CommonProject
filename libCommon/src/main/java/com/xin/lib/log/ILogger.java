package com.xin.lib.log;

/**
 * <br> ClassName:    ILogger
 * <br> Description:  日志接口
 * <br>
 * <br> @author:      zouxinjie
 * <br> Date:         2019/8/17 11:29
 */
public interface ILogger {

    /**
     *<br> Description: 打印详细信息，自定义Tag方法
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:37
     */
    void v(String tag, String msg);

    /**
     *<br> Description: 打印详细信息，自定义Tag方法，还有异常信息
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:37
     */
    void v(String tag, String msg, Throwable tr);

    /**
     *<br> Description: 打印debug信息，自定义Tag方法
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:37
     */
    void d(String tag, String msg);

    /**
     *<br> Description: 打印debug信息，自定义Tag方法，还有异常信息
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:37
     */
    void d(String tag, String msg, Throwable tr);

    /**
     *<br> Description: 打印信息，自定义Tag方法
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:37
     */
    void i(String tag, String msg);

    /**
     *<br> Description: 打印信息，自定义Tag方法，还有异常信息
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:37
     */
    void i(String tag, String msg, Throwable tr);

    /**
     *<br> Description: 打印警告信息，自定义Tag方法
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:37
     */
    void w(String tag, String msg);

    /**
     *<br> Description: 打印警告信息，自定义Tag方法，还有异常信息
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:37
     */
    void w(String tag, String msg, Throwable tr);

    /**
     *<br> Description: 打印错误信息，自定义Tag方法
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:37
     */
    void e(String tag, String msg);

    /**
     *<br> Description: 打印错误信息，自定义Tag方法，还有异常信息
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:37
     */
    void e(String tag, String msg, Throwable tr);

    /**
     *<br> Description: JSON格式，打印信息，自定义tag
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/17 11:37
     */
    void json(String tag, String msg);

}
