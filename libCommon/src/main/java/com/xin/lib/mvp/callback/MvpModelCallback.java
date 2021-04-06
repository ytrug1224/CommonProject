package com.xin.lib.mvp.callback;


/**
 * <br> ClassName:    MvpModelCallback
 * <br> Description:  网络请求统一P层回调处理
 * <br>
 * <br> Date:         2020/8/26 8:52
 */
public abstract class MvpModelCallback<T> implements IMvpModelCallback<T> {


    @Override
    public void onSuccess(T t, Object... list) {

    }

    @Override
    public void onFailure(Object e) {

    }


}

