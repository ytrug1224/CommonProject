package com.xin.lib.mvp.extend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.AlphaAnimation;

import androidx.annotation.Nullable;

import com.xin.lib.mvp.R;
import com.xin.lib.mvp.base.AbstractMvpFragment;
import com.xin.lib.mvp.base.AbstractMvpPresenter;

/**
 * <br> ClassName:    AbstractLazyFragment
 * <br> Description:  懒加载 Fragment
 * <br>
 * <br> Date:         2020/8/30 10:52
 */
public abstract class AbstractLazyFragment<T extends AbstractMvpPresenter>
        extends AbstractMvpFragment<T> {
    protected ViewGroup mRootView;
    protected ViewStub mVsContent;
    protected boolean mIsLoadUi = false;
    private boolean mIsDestroy = false;
    protected Bundle mSavedInstanceState;

    /**
    *<br> Description: 延迟加载UI
    *<br> Author:      zouxinjie
    *<br> Date:        2020/8/30 10:52
    */
    protected abstract View onLoadUI();


    @Nullable
    @Override
    public View onBindView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        this.mSavedInstanceState = savedInstanceState;
        mIsLoadUi = false;
        mIsDestroy = false;
        View view = inflater.inflate(R.layout.mvp_lazy_fragment, null);
        mRootView = view.findViewById(R.id.frt_main);
        mVsContent = view.findViewById(R.id.vs_content);
        return view;
    }

    /**
     *<br> Description: 初始化UI
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/30 10:52
     */
    private void setupUI() {
        mVsContent.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mIsLoadUi || mIsDestroy){
                    return;
                }
                View view = onLoadUI();
                mIsLoadUi = true;
                if (view != null) {
                    AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                    alphaAnimation.setFillAfter(true);
                    alphaAnimation.setDuration(150);
                    view.startAnimation(alphaAnimation);
                }
            }
        }, 150);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIsLoadUi) {
            onLazyResume();
        }
    }

    /**
     *<br> Description: onResume
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/30 10:52
     */
    protected void onLazyResume() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsDestroy = true;
    }

    @Override
    public final void onPause() {
        super.onPause();
        if (mIsLoadUi) {
            onLazyPause();
        }
    }

    /**
     *<br> Description: onPause
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/30 10:51
     */
    protected void onLazyPause() {

    }

    @Override
    public final void onVisibleToUserChanged(boolean isVisibleToUser, boolean invokeInResumeOrPause) {
        super.onVisibleToUserChanged(isVisibleToUser, invokeInResumeOrPause);
        if (mIsLoadUi) {
            onVisibilityChangedToUser(isVisibleToUser, invokeInResumeOrPause);
        } else if (isVisibleToUser && isVisible()) {
            setupUI();
        }
    }

    /**
     *<br> Description: 对用户是否可见
     *<br> Author:      zouxinjie
     *<br> Date:        2020/8/30 10:52
     */
    protected void onVisibilityChangedToUser(boolean isVisibleToUser, boolean invokeInResumeOrPause) {


    }
}
