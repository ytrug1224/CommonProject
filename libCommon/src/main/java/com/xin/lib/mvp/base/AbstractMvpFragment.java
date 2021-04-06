package com.xin.lib.mvp.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.xin.lib.base.AbstractFragment;


/**
 * <br> ClassName:    AbstractMvpFragment
 * <br> Description:  MVP框架 - View - Fragment 基类
 * <br>
 */
public abstract class AbstractMvpFragment<T extends com.xin.lib.mvp.base.AbstractMvpPresenter>
        extends AbstractFragment implements com.xin.lib.mvp.base.IMvpView {

    protected T mBasePresenter;
    protected Activity mActivity;

    /**
     *<br> Description: 创建Presenters
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/17 14:52
     */
    protected abstract T createPresenter();

    public T getCurrentPresenter() {
        return mBasePresenter;
    }

    @Override
    public void onAttach(Context context) {
        mActivity = (Activity) context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {
        mBasePresenter = createPresenter();
        if (mBasePresenter != null) {
            mBasePresenter.attachView(this);
        }
        return onBindView(inflater,container,savedInstanceState);
    }

    /**
     *<br> Description: 使用onBindView代替onCreateView
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/17 14:53
     */
    protected abstract View onBindView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        onUnbindView();
        if (mBasePresenter != null) {
            mBasePresenter.detachView();
        }
    }

    /**
     *<br> Description: 使用onUnbindView代替onDestroyView
     *                  避免在mBasePresenter.detachView()之后，
     *                  网络请求数据回来时调用view而报错
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/17 14:52
     */
    protected void onUnbindView(){}

}
