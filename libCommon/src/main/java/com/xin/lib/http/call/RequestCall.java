package com.xin.lib.http.call;

import android.text.TextUtils;

import com.xin.lib.http.HttpTools;
import com.xin.lib.http.callback.AbstractCallback;
import com.xin.lib.http.converter.IConverter;
import com.xin.lib.http.converter.IRequestConverter;
import com.xin.lib.http.exception.ApiException;
import com.xin.lib.http.interceptor.VSmartInterceptor;
import com.xin.lib.http.progress.ProgressListener;
import com.xin.lib.log.Logger;
import com.xin.lib.utils.AppUtils;
import com.xin.lib.utils.NetworkUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <br> ClassName:    RequestCall
 * <br> Description:  请求实体 RequestCall
 * <br>
 * <br> @author:      zouxinjie
 * <br> Date:         2020/8/21 15:01
 */
public abstract class RequestCall {

    private String mUrl;

    private String mTaskId;
    private Call mCall;

    private long mReadTimeOut;
    private long mWriteTimeOut;
    private long mConnectTimeout;

    private volatile boolean mCanceled;

    /**
     * VSmart 独立实例标识
     */
    private String mFlag;

    /**
     * 进度回调
     */
    private ProgressListener mProgressListener;

    /**
     * 辅助标签
     */
    private String mTag;

    /**
     * 当前请求的AbstractCallback
     */
    private AbstractCallback mAbstractCallback;

    /**
     * 当前请求的IConverter
     */
    private IConverter mIConverter;

    /**
     * Url 参数拼接
     */
    private LinkedHashMap<String, String> mUrlParams = new LinkedHashMap<>();

    /**
     * VSmart拦截器
     */
    private VSmartInterceptor vSmartInterceptor;

    /**
     * 请求转换器
     * （暂时，只用于PostRequestCall - json 请求）
     */
    private IRequestConverter iRequestConverter;

   /**
     * 请求管理
     */
//    private Reference<IRequestManager> mIRequestManager;

    /**
     *<br> Description: 构造函数
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:48
     */
    public RequestCall(String url) {
        this.mUrl = url;
        this.mTaskId = url;
    }
//
//    /**
//     *<br> Description: 设置重新加载请求
//     *<br> Author:      zouxinjie
//     *<br> Date:        2020/6/13 15:45
//     *
//     * @param iSetReloadAction ISetReloadAction
//     * @return RequestCall
//     */
//    public RequestCall setReload(ISetReloadAction iSetReloadAction) {
//        if (iSetReloadAction != null) {
//            iSetReloadAction.setReloadAction(this);
//        }
//        return this;
//    }
//
//    /**
//     *<br> Description: 设置请求管理器
//     *<br> Author:      zouxinjie
//     *<br> Date:        2020/6/23 14:11
//     *
//     * @param iRequestManager IRequestManager
//     * @return RequestCall
//     */
//    public RequestCall setRequestManager(IRequestManager iRequestManager) {
//        if (iRequestManager != null) {
//            this.mIRequestManager = new WeakReference<IRequestManager>(iRequestManager);
//        }
//        return this;
//    }

    /**
     *<br> Description: 设置VSmart独立实例标识
     *<br> Author:      zouxinjie
     *<br> Date:        2020/1/3 15:47
     */
    public RequestCall setFlag(String flag) {
        this.mFlag = flag;
        return this;
    }

    /**
     *<br> Description: 获取VSmart独立实例标识
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/22 15:24
     */
    public String getFlag() {
        return this.mFlag;
    }

    /**
     *<br> Description: 设置辅助标签
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/22 15:23
     */
    public RequestCall setTag(String tag) {
        this.mTag = tag;
        return this;
    }

    /**
     *<br> Description: 获取辅助标签
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/22 15:24
     */
    public String getTag() {
        return this.mTag;
    }

    /**
     *<br> Description: 设置Url
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:48
     */
    public RequestCall setUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            this.mUrl = url;
            this.mTaskId = url;
        }
        return this;
    }

    /**
     *<br> Description: 设置读取超时
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:48
     */
    public RequestCall setReadTimeout(long readTimeOut) {
        this.mReadTimeOut = readTimeOut;
        return this;
    }

    /**
     *<br> Description: 设置写入超时
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:48
     */
    public RequestCall setWriteTimeOut(long writeTimeOut) {
        this.mWriteTimeOut = writeTimeOut;
        return this;
    }

    /**
     *<br> Description: 设置socket连接超时
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:49
     */
    public RequestCall setConnectTimeout(long connectTimeout) {
        this.mConnectTimeout = connectTimeout;
        return this;
    }

    /**
     *<br> Description: 增加进度回调监听
     *<br> Author:      zouxinjie
     *<br> Date:        2020/11/6 16:48
     */
    public RequestCall addProgressListener(ProgressListener progressListener) {
        this.mProgressListener = progressListener;
        return this;
    }

    /**
     *<br> Description: 增加 Url 请求参数 - long
     *<br> Author:      zouxinjie
     *<br> Date:        2020/3/3 16:06
     */
    public RequestCall addUrlParams(String key, long value) {
        return addUrlParams(key, String.valueOf(value));
    }

    /**
     *<br> Description: 增加 Url 请求参数 - int
     *<br> Author:      zouxinjie
     *<br> Date:        2020/3/3 16:06
     */
    public RequestCall addUrlParams(String key, int value) {
        return addUrlParams(key, String.valueOf(value));
    }

    /**
     *<br> Description: 增加 Url 请求参数 - String
     *<br> Author:      zouxinjie
     *<br> Date:        2020/3/3 16:06
     */
    public RequestCall addUrlParams(String key, String value) {
        mUrlParams.put(key, value);
        return this;
    }

    /**
     *<br> Description: 批量增加 Url 请求参数 -
     *<br> Author:      zouxinjie
     *<br> Date:        2020/3/3 16:06
     */
    public RequestCall addUrlParams(Map<String, String> paramsMap) {
        if (paramsMap != null && !paramsMap.isEmpty()) {
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                mUrlParams.put(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    /**
     *<br> Description: 拼接 Url 请求参数
     *<br> Author:      zouxinjie
     *<br> Date:        2020/3/3 16:09
     */
    public String getGenerateUrl() {
        if (mUrlParams.isEmpty()) {
            return getUrl();
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(getUrl());
            if (getUrl().indexOf('&') > 0 || getUrl().indexOf('?') > 0) {
                sb.append("&");
            } else {
                sb.append("?");
            }
            for (Map.Entry<String, String> urlParams : mUrlParams.entrySet()) {
                //对参数进行 utf-8 编码,防止头信息传中文
                String urlValue = URLEncoder.encode(urlParams.getValue(), "UTF-8");
                sb.append(urlParams.getKey()).append("=").append(urlValue).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }
        return getUrl();
    }


    /**
     *<br> Description: 包装OkHttpClient
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:49
     */
    private OkHttpClient generateClient() {
        if (mReadTimeOut <= 0 && mWriteTimeOut <= 0 && mConnectTimeout <= 0) {
            return HttpTools.getClient(getFlag());
        } else {
            OkHttpClient.Builder newClientBuilder = HttpTools.getClient(getFlag()).newBuilder();
            if (mReadTimeOut > 0) {
                newClientBuilder.readTimeout(mReadTimeOut, TimeUnit.MILLISECONDS);
            }
            if (mWriteTimeOut > 0) {
                newClientBuilder.writeTimeout(mWriteTimeOut, TimeUnit.MILLISECONDS);
            }
            if (mConnectTimeout > 0) {
                newClientBuilder.connectTimeout(mConnectTimeout, TimeUnit.MILLISECONDS);
            }
            return newClientBuilder.build();
        }
    }


    /**
     *<br> Description: 获取任务Id
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:49
     */
    public String getTaskId() {
        return TextUtils.isEmpty(mTaskId) ? "" : mTaskId;
    }

    /**
     *<br> Description: 获取Url
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:49
     */
    public String getUrl() {
        return TextUtils.isEmpty(mUrl) ? "" : mUrl;
    }

    /**
     *<br> Description: 获取OkHttp的Call
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:49
     */
    public Call getCall() {
        return  mCall;
    }

    /**
     *<br> Description: 获取进度回调监听
     *<br> Author:      zouxinjie
     *<br> Date:        2020/11/6 17:48
     */
    public ProgressListener getProgressListener() {
        return this.mProgressListener;
    }

    /**
     *<br> Description: 重新请求
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:49
     */
    public RequestCall retry() {
        if (this.mCall != null) {
            this.mCall.cancel();
            this.mCall = null;
        }
        if (mAbstractCallback != null) {
            return execute(mAbstractCallback);
        }
        return null;
    }
//
//    /**
//     *<br> Description: 将RequeCall加入请求管理器
//     *<br> Author:      zouxinjie
//     *<br> Date:        2020/6/23 14:17
//     *
//     * @param requestCall RequestCall
//     */
//    private void addToRequestManager(RequestCall requestCall) {
//        if (this.mIRequestManager != null && requestCall != null) {
//            this.mIRequestManager.get().addCalls(requestCall);
//        }
//    }
//
//    /**
//     *<br> Description: 将RequeCall从请求管理器移除
//     *<br> Author:      zouxinjie
//     *<br> Date:        2020/6/23 14:17
//     *
//     * @param requestCall RequestCall
//     */
//    private void removeFromReqManager(RequestCall requestCall) {
//        if (this.mIRequestManager != null && requestCall != null) {
//            this.mIRequestManager.get().removeCall(requestCall);
//        }
//    }


    /**
     *<br> Description: 兼容 RxJava 扩展
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/22 9:01
     */
    @SuppressWarnings("unchecked")
    public <T> T adapt(CallAdapter<T> adapter) {
        return adapter.adapt(this);
    }


    /**
     *<br> Description: 取消请求
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:49
     */
    public void cancel() {
        mCanceled = true;
        if (this.mCall != null) {
            this.mCall.cancel();
        }
    }

    /**
     *<br> Description: 是否取消了请求
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:49
     */
    public boolean isCanceled() {
        if (mCanceled) {
            return true;
        }
        synchronized (this) {
            return this.mCall != null && this.mCall.isCanceled();
        }
    }

    /**
     *<br> Description: 包装okhttp3.Request，区分post\get
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:49
     */
    public abstract Request generateRequest();


    /**
     *<br> Description: 增加 VSmartInterceptor 拦截器
     *<br> Author:      zouxinjie
     *<br> Date:        2020/4/18 15:34
     */
    public RequestCall addInterceptor(VSmartInterceptor interceptor) {
        vSmartInterceptor = interceptor;
        return this;
    }

    /**
     *<br> Description: 增加 请求拦截器
     *<br> Author:      zouxinjie
     *<br> Date:        2020/4/20 11:20
     */
    public RequestCall addRequestConverter(IRequestConverter converter) {
        iRequestConverter = converter;
        return this;
    }

    /**
     *<br> Description: 获取 请求拦截器
     *<br> Author:      zouxinjie
     *<br> Date:        2020/4/20 11:21
     */
    public IRequestConverter getRequestConverter() {
        return iRequestConverter;
    }


    /**
     *<br> Description: 执行请求 - 异步
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:49
     */
    public RequestCall execute(final AbstractCallback abstractCallback) {
        //执行拦截器
        if (vSmartInterceptor != null) {
            vSmartInterceptor.intercept(new VSmartInterceptor.Callback() {
                @Override
                public void onResult() {
                    _execute(abstractCallback);
                }
            });

        } else {
            return _execute(abstractCallback);
        }
        return this;
    }

    /**
     *<br> Description: 执行请求
     *<br> Author:      zouxinjie
     *<br> Date:        2020/4/18 15:44
     */
    public RequestCall _execute(AbstractCallback abstractCallback) {
        mAbstractCallback = abstractCallback;
        mAbstractCallback.onStart(RequestCall.this);

        if (AppUtils.isMainThread() &&
                !NetworkUtils.isNetWorkAvailable(AppUtils.getContext())) {
            //TODO mTaskId 处理重新刷新
            sendOnFailure(mAbstractCallback, null, new ApiException(ApiException.CODE_NO_NETWORK, "无网络连接"));
//            addToRequestManager(this);
            return this;
        }

        this.mCall = generateClient().newCall(generateRequest());
        if (mCanceled) {
//            removeFromReqManager(this);
            return this;
        }
        this.mCall.enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
//                removeFromReqManager(RequestCall.this);

                AppUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (e instanceof SocketTimeoutException) {
                            sendOnFailure(mAbstractCallback, call,
                                    new ApiException(ApiException.CODE_OKHTTP_SOCKET_TIMEOUT, e, mUrl));
                        } else if (e instanceof SocketException) {
                            sendOnFailure(mAbstractCallback, call,
                                    new ApiException(ApiException.CODE_OKHTTP_SOCKET_EXP, e, mUrl));
                        } else if (e instanceof SSLException) {
                            sendOnFailure(mAbstractCallback, call,
                                    new ApiException(ApiException.CODE_OKHTTP_SSL_EXP, e, mUrl));
                        } else {
                            sendOnFailure(mAbstractCallback, call,
                                    new ApiException(ApiException.CODE_OKHTTP_FAILURE, e, mUrl));
                        }
                    }
                });

            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                try {
//                    removeFromReqManager(RequestCall.this);

                    if (response.isSuccessful()) {
                        final Object o = mAbstractCallback.parseResponse(call, response);
                        AppUtils.runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                sendOnSuccess(mAbstractCallback, o);
                            }
                        });
                    } else {
                        AppUtils.runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                sendOnFailure(mAbstractCallback, call,
                                        new ApiException(ApiException.CODE_OKHTTP_NO_SUCCESS, response.message(), response.code()));
                            }
                        });

                    }

                } catch (final Exception exp) {
                    AppUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if (exp instanceof ApiException) {
                                sendOnFailure(mAbstractCallback, call, (ApiException) exp);
                            } else {
                                sendOnFailure(mAbstractCallback, call,
                                        new ApiException(ApiException.CODE_OTHER_EXCEPTION, exp));
                            }
                        }
                    });
                } finally {
                    if (response.body() != null) {
                        response.body().close();
                    }
                }
            }
        });

//        addToRequestManager(this);
        return this;
    }


    /**
     *<br> Description: 执行请求 - 同步
     *<br> Author:      zouxinjie
     *<br> Date:        2020/11/6 15:19
     */
    public Response execute() throws IOException {
        this.mCall = generateClient().newCall(generateRequest());
        return this.mCall.execute();
    }


    /**
     *<br> Description: 发送请求成功结果
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:50
     */
    private void sendOnSuccess(AbstractCallback abstractCallback, Object object) {
        abstractCallback.onSuccess(object);
        abstractCallback.onComplete(this, object);
    }

    /**
     *<br> Description: 发送请求失败结果
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:50
     */
    private void sendOnFailure(AbstractCallback abstractCallback, Call call, ApiException apiException) {
        abstractCallback.onFailure(this, apiException);
        abstractCallback.onComplete(this, null);
    }

}
