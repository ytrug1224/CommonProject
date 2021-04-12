package com.xin.app.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.xin.app.home.web.CallJavascriptActivity;
import com.xin.app.home.web.JavascriptCallNativeActivity;
import com.xin.lib.arouter.HomeRouterConstants;
import com.xin.lib.constants.AppConstants;
import com.xin.lib.mvp.base.AbstractMvpActivity;
import com.xin.lib.mvp.base.AbstractMvpPresenter;

@Route(path = HomeRouterConstants.HOME_ACTIVITY)
public class HomeActivity extends AbstractMvpActivity {

    @Autowired(name = AppConstants.ARG1)
    String mHomeText;

    @Override
    protected AbstractMvpPresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_web_view);

        Toast.makeText(this, mHomeText, Toast.LENGTH_SHORT).show();

        findViewById(R.id.home_call_js).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CallJavascriptActivity.class));
            }
        });

        findViewById(R.id.home_js_call_native).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, JavascriptCallNativeActivity.class));
            }
        });
    }

    @Override
    public void showLoading(String tips) {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showToast(String info) {

    }
}