package com.xin.lib.mvp.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.xin.lib.base.AbstractActivity;

/**
 * <br> ClassName:    AbstractMvpActivity
 * <br> Description:  MVP框架 - View - Activity 基类
 * <br>
 */
public abstract class AbstractMvpActivity<T extends AbstractMvpPresenter>
        extends AbstractActivity implements IMvpView {

    protected T mCurrentPresenter;
    /**
     * 自定义一个标志位，
     * 标记Activity的状态是否已经保存
     */
    private boolean mStateSaved = false;

    /**
     *<br> Description: 创建Presenters
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/17 14:48
     */
    protected abstract T createPresenter();

    public T getCurrentPresenter() {
        return mCurrentPresenter;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentPresenter = createPresenter();
        if (mCurrentPresenter != null) {
            mCurrentPresenter.attachView(this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mStateSaved = true;
    }

    @Override
    protected void onResume() {
        mStateSaved = false;
        super.onResume();
    }

    /**
     *<br> Description: 获取activity的保存状态，是否已经保存了Activity的状态。
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/17 14:51
     */
    public boolean isStateSaved() {
        return mStateSaved;
    }

    @Override
    public void onFinish() {
        if (!isFinishing()) {
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCurrentPresenter != null) {
            mCurrentPresenter.detachView();
        }
    }

}
