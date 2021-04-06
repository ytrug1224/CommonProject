package com.xin.lib.http;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.xin.lib.http.call.GetRequestCall;
import com.xin.lib.http.call.PostRequestCall;
import com.xin.lib.http.call.RangeRequestCall;
import com.xin.lib.http.interceptor.NotIOExceptionInterceptor;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * <br> ClassName:    VSmart
 * <br> Description:  VSmart 网络请求库
 * <br>
 * <br> @author:      zouxinjie
 * <br> Date:         2020/8/21 14:42
 */
public final class HttpTools {
    private static Context mContext;
    private static boolean isDebug = false;
    private volatile static OkHttpClient client;
    private static OkHttpClient.Builder customBuilder;

    private static Map<String, OkHttpClient.Builder> builderMap = new ConcurrentHashMap<>();
    private static Map<String, OkHttpClient> clientMap = new ConcurrentHashMap<>();

    public static void init(Application application) {
        mContext = application;
    }

    /**
     *<br> Description: 设置自定义OkHttpClient.Builder
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:45
     */
    public static void initOkHttp(OkHttpClient.Builder builder) {
        if (builder == null) {
            throw new IllegalArgumentException("builder must not be null.");
        }
        synchronized (HttpTools.class) {
            if (customBuilder != null) {
                throw new IllegalStateException("customBuilder already exists.");
            }
            customBuilder = builder;
        }
    }

    /**
     *<br> Description: 新增独立VSmart请求实例
     *<br> Author:      zouxinjie
     *<br> Date:        2020/1/3 11:36
     */
    public static void addOkHttp(String flag, OkHttpClient.Builder builder) {
        if (TextUtils.isEmpty(flag)) {
            throw new IllegalArgumentException("tag must not be null.");
        }
        if (builder == null) {
            throw new IllegalArgumentException("builder must not be null.");
        }
        builderMap.put(flag, builder);
    }

    /**
     *<br> Description: 返回一个OkHttpClient
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:46
     */
    public static OkHttpClient getClient() {
        if (client == null) {
            synchronized (HttpTools.class) {
                if (client == null) {
                    if (customBuilder != null) {
                        customBuilder.addInterceptor(new NotIOExceptionInterceptor());
                        client = customBuilder.build();
                    } else {
                        OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
                        mBuilder.addInterceptor(new NotIOExceptionInterceptor());
                        mBuilder.sslSocketFactory(createSSLSocketFactory());
                        mBuilder.retryOnConnectionFailure(false);
                        client = mBuilder.build();
                    }

                }
            }
        }
        return client;
    }

    /**
     *<br> Description: 根据 tag 返回一个OkHttpClient
     *<br> Author:      zouxinjie
     *<br> Date:        2020/1/3 11:37
     */
    public static OkHttpClient getClient(String flag) {
        if (TextUtils.isEmpty(flag)) {
            return getClient();
        }
        OkHttpClient client = clientMap.get(flag);
        if (client == null) {
            synchronized (HttpTools.class) {
                OkHttpClient.Builder builder = builderMap.get(flag);
                if (builder != null) {
                    builder.addInterceptor(new NotIOExceptionInterceptor());
                    client = builder.build();
                    clientMap.put(flag, client);
                } else {
                    throw new IllegalArgumentException("Can't find OkHttpClient.Builder named \"" + flag +"\", please call addOkHttp() first.");
                }
            }
        }
        return client;
    }

    /**
     *<br> Description: post （Json）请求
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:46
     */
    public static PostRequestCall post(String url) {
        return new PostRequestCall(url);
    }

    /**
     *<br> Description: get 请求
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:46
     */
    public static GetRequestCall get(String url) {
        return new GetRequestCall(url);
    }

    /**
     *<br> Description: Range 断点下载请求
     *<br> Author:      zouxinjie
     *<br> Date:        2020/10/14 11:01
     *
     * @param url      请求的Get地址
     * @param downloadPath 下载路径
     * @param fileName     文件名，必须与Url一一对应，否则会出现多个请求地址下载到同一文件
     * @return
     */
    public static RangeRequestCall range(String url, String downloadPath, String fileName) {
        return new RangeRequestCall(url, downloadPath, fileName);
    }

    /**
     *<br> Description: 设置为调试模式
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:46
     */
    public static void setDebugMode(boolean mode) {
        isDebug = mode;
    }

    /**
     *<br> Description: 获取模式状态
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:46
     */
    public static boolean getDebugMode() {
        return isDebug;
    }

    /**
     *<br> Description: createSSLSocketFactory
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:46
     */
    public static SSLSocketFactory createSSLSocketFactory() {

        SSLSocketFactory sSLSocketFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllManager()},
                    new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sSLSocketFactory;
    }

    /**
     *<br> Description: TrustAllManager
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:46
     */
    private static class TrustAllManager implements X509TrustManager {

        /**
         * checkClientTrusted
         */
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
                throws java.security.cert.CertificateException {
        }

        /**
         * checkClientTrusted
         */
        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
                throws java.security.cert.CertificateException {
        }

        /**
         * checkClientTrusted
         */
        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[0];
        }
    }
}
