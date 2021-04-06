package com.xin.lib.http.exception;

import android.text.TextUtils;

import com.xin.lib.http.Response;


/**
 * <br> ClassName:    ApiException
 * <br> Description:  统一异常类
 * <br>
 * <br> @author:      zouxinjie
 * <br> Date:         2020/8/21 15:44
 */
public class ApiException extends Exception {

    /***无网络连接***/
    public static final int CODE_NO_NETWORK = 4000;

    /***OkHttp 回调 onFailure***/
    public static final int CODE_OKHTTP_FAILURE = 4001;
    /***OkHttp response.isSuccessful() == false***/
    public static final int CODE_OKHTTP_NO_SUCCESS = 4002;

    /***response.code == 299***/
    public static final int CODE_REQ_TOOFREQUEST = 4003;
    /***!response.isSuccessful()***/
    public static final int CODE_REQ_FAILURE = 4004;

    /***OkHttp 回调 onFailure——SocketTimeoutException***/
    public static final int CODE_OKHTTP_SOCKET_TIMEOUT = 4005;
    /***OkHttp 回调 onFailure——SocketException***/
    public static final int CODE_OKHTTP_SOCKET_EXP = 4006;
    /***OkHttp 回调 onFailure——SSLException***/
    public static final int CODE_OKHTTP_SSL_EXP = 4007;

    /***其余异常***/
    public static final int CODE_OTHER_EXCEPTION = 7000;

    /**
     * 错误码
     */
    private int mCode;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 请求URL
     */
    private String mUrl;

    /**
     * 异常处理Object
     */
    private Object mResultObject;


    /**
     *<br> Description: 构造函数
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:47
     */
    public ApiException(int code, String message, Throwable throwable) {
        super(message, throwable);
        this.mCode = code;
    }

    /**
     *<br> Description: 构造函数
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:47
     */
    public ApiException(int code, String message) {
        super(message);
        this.mCode = code;
    }

    /**
     *<br> Description: 构造函数
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:47
     */
    public ApiException(int code, Throwable throwable) {
        super(throwable);
        this.mCode = code;
    }

    /**
     *<br> Description: 构造函数
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:47
     */
    public ApiException(int code, Throwable throwable, String url) {
        super(throwable);
        this.mCode = code;
        this.mUrl = url;
    }

    /**
     *<br> Description: 构造函数
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:47
     */
    public ApiException(int code, String message, Object resultObject) {
        super(message);
        this.mCode = code;
        this.mResultObject = resultObject;
    }

    /**
     *<br> Description: 构造函数
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:47
     */
    public ApiException(Response<?> response) {
        super(getMessage(response));
        this.mCode = response.code();
        this.message = response.message();
    }


    public int getCode() {
        return mCode;
    }

    public void setCode(int code) {
        this.mCode = code;
    }

    @Override
    public String getMessage() {
        if (TextUtils.isEmpty(message)) {
            if (TextUtils.isEmpty(super.getMessage())) {
                return "Unknown Error!";
            } else {
                return super.getMessage();
            }
        } else {
            return message;
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public Object getResultObject() {
        return mResultObject;
    }

    public void setResultObject(Object resultObject) {
        this.mResultObject = resultObject;
    }

    /**
     *<br> Description:  获取Response信息
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:47
     */
    private static String getMessage(Response<?> response) {
        if (response == null) {
            return "Response is null!!! ";
        }
        return "HTTP " + response.code() + " " + response.message();
    }
}
