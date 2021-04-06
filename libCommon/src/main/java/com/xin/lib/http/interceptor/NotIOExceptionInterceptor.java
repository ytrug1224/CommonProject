package com.xin.lib.http.interceptor;


import com.xin.lib.http.HttpTools;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * <br> ClassName:    NotIOExceptionInterceptor
 * <br> Description:  修复bug，异常处理拦截器
 *                      okhttp AsyncCall only catch the IOException, other exception will occur crash，this Interceptor impl can transform all exception to IOException
 *  *                   see : https://github.com/square/okhttp/issues/3477
 * <br>
 * <br> @author:      zouxinjie
 * <br> Date:         2020/8/21 15:45
 */
public class NotIOExceptionInterceptor implements Interceptor {
    private static final String TAG = "Smart.OkHttp3";

    private CatchListener mListener;

    public NotIOExceptionInterceptor() {
    }

    public NotIOExceptionInterceptor(CatchListener listener) {
        this.mListener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        try {
            return chain.proceed(chain.request());
        } catch (Throwable e) {
            if (HttpTools.getDebugMode()) {
                e.printStackTrace();
            }

            if (e instanceof IOException) {
                throw e;
            }

            if (mListener != null) {
                mListener.reportException();
            }
            throw new IOException(e);
        }
    }

    private interface CatchListener {
        void reportException();
    }
}