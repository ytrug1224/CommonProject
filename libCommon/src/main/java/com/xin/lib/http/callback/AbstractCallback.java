package com.xin.lib.http.callback;

import com.xin.lib.http.call.RequestCall;
import com.xin.lib.http.converter.IConverter;
import com.xin.lib.http.exception.ApiException;

/**
 * <br> ClassName:    AbstractCallback
 * <br> Description:  对OkHttp Callback 的扩展
 * <br>
 * <br> @author:      zouxinjie
 * <br> Date:         2020/8/21 15:43
 */
public abstract class AbstractCallback<T> implements IConverter<T> {

    /**
     *<br> Description: 发起请求前（主线程）
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:46
     */
    public void onStart(RequestCall requestCall) {

    }

    /**
     *<br> Description: 请求失败（主线程）
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:46
     */
    public abstract void onFailure(RequestCall requestCall, ApiException e);

    /**
     *<br> Description: 请求成功（主线程）
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:46
     */
    public abstract void onSuccess(T response);

    /**
     *<br> Description: （下载）进度（主线程）
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:46
     */
    public void onProgress(float progress, long total) {

    }

    /**
     *<br> Description: 请求完成（成功、失败都会调用，主线程）
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/21 15:47
     */
    public void onComplete(RequestCall requestCall, T response) {

    }
}
