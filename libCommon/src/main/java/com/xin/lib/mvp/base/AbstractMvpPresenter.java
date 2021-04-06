package com.xin.lib.mvp.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * <br> ClassName:    AbstractMvpPresenter
 * <br> Description:  MVP框架 - Presenter基类
 * <br>
 * <br> Date:         2020/8/17 14:46
 */
public abstract class AbstractMvpPresenter<T extends IMvpView, E extends IMvpModel> {

    protected E mCurrentModel;

    /**
     *  View接口类型弱引用
     */
    protected Reference<T> mViewRef;

    /**
     * 建立 p-v 关联
     */
    public void attachView(T view) {
        mViewRef = new WeakReference<T>(view);
        mCurrentModel = createCurrentModel();
    }

    /**
     * 获取view
     */
    protected T getView() {
        return mViewRef.get();
    }

    /**
     * 判断是否与View建立了关联
     */
    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    /**
     *<br> Description: 创建Model
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/19 8:54
     */
    protected abstract E createCurrentModel();

    public E getCurrentModel() {
        return mCurrentModel;
    }


    /**
     * 解除关联
     */
    public void detachView() {
        if (mCurrentModel != null) {
            mCurrentModel.onDestroy();
            mCurrentModel = null;
        }
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }
}
