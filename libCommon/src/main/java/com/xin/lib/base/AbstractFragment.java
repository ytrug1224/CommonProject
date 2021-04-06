package com.xin.lib.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * <br> ClassName:    AbstractFragment
 */
public class AbstractFragment extends Fragment implements FragmentUserVisibleController.UserVisibleCallback{

    private FragmentUserVisibleController mUserVisibleController;
    /**
     * 显示的是否是当前页
     */
    protected boolean mIsVisible = false;

    /**
     *<br> Description: 构造方法
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/30 10:51
     */
    public AbstractFragment() {
        mUserVisibleController = new FragmentUserVisibleController(this, this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUserVisibleController.activityCreated();
    }

    @Override
    public void setWaitingShowToUser(boolean waitingShowToUser) {
        mUserVisibleController.setWaitingShowToUser(waitingShowToUser);
    }

    @Override
    public boolean isWaitingShowToUser() {
        return mUserVisibleController.isWaitingShowToUser();
    }

    @Override
    public boolean isVisibleToUser() {
        return mUserVisibleController.isVisibleToUser();
    }

    @Override
    public void callSuperSetUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    public boolean isPageVisible() {
        return mIsVisible;
    }

    /**
     * 当Fragment对用户可见或不可见的就会回调此方法，可以在这个方法里记录页面显示日志或初始化页面
     * 解决回退到Fragment，不调用setUserVisibleHint的情况
     */
    @Override
    public void onVisibleToUserChanged(boolean isVisibleToUser, boolean invokeInResumeOrPause) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        mUserVisibleController.setUserVisibleHint(isVisibleToUser);
        mIsVisible = isVisibleToUser && isVisible();
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();
        mUserVisibleController.resume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mUserVisibleController.pause();
    }

}
