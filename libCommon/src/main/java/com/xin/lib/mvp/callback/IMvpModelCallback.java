package com.xin.lib.mvp.callback;


/**
 * <br> ClassName:    IMvpModelCallback
 * <br> Description:  网络请求统一P层接口回调
 * <br>
 * <br> Date:         2020/8/26 8:52
 */
public interface IMvpModelCallback<T> {

    void onSuccess(T t);


    void onSuccess(T t, Object... list);


    void onFailure(Object e);
}
