package com.xin.lib.mvp.base;

/**
 * <br> ClassName:    IMvpView
 * <br> Description:  MVP框架 - View接口
 * <br>
 * <br> Date:         2020/8/17 14:41
 */
public interface IMvpView {

    void showLoading(String tips);

    void dismissLoading();

    void showToast(String info);

    void onFinish();

}
