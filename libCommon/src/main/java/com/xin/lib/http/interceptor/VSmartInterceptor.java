package com.xin.lib.http.interceptor;

/**
 * <br> ClassName:    VSmartInterceptor
 * <br> Description:  VSmart 拦截器-异步
 * <br>
 * <br> @author:      zouxinjie
 * <br> Date:         2020/4/18 15:29
 */
public interface VSmartInterceptor {

    void intercept(Callback callback);

    interface Callback {
        void onResult();
    }
}
