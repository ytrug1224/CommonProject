package com.xin.lib.base;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


/**
 * <br> ClassName:    AbstractActivity
 *  目前什么也不想做。。
 */
public abstract class AbstractActivity extends AppCompatActivity {

    private View mMainContent;

    /**
     * DrawerLayout 模式使用
     */
    private LinearLayout baseMainHolder;
    private LinearLayout baseLeftHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     *<br> Description: 重写setContentView 方法
     *<br> Author:      zouxinjie
     *<br> Date:        2019/8/28 16:38
     */
    @Override
    public void setContentView(int layoutResID) {
        //增加内容
        mMainContent = getLayoutInflater().inflate(layoutResID, null);
        this.setContentView(mMainContent);
    }

    @Override
    public void setContentView(View view) {

    }


}
