package com.xin.project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.xin.lib.arouter.DiscoveryRouterConstants;
import com.xin.lib.arouter.HomeRouterConstants;
import com.xin.lib.arouter.MainRouterConstants;
import com.xin.lib.constants.AppConstants;
import com.xin.lib.mvp.base.AbstractMvpActivity;
import com.xin.lib.mvp.base.AbstractMvpPresenter;

@Route(path = MainRouterConstants.MAIN_ACTIVITY)
public class MainActivity extends AbstractMvpActivity implements View.OnClickListener {

    private Button mHomeBtn;
    private Button mDiscoveryBtn;

    @Override
    protected AbstractMvpPresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHomeBtn = findViewById(R.id.to_home_btn);
        mDiscoveryBtn = findViewById(R.id.to_discovery_btn);

        mHomeBtn.setOnClickListener(this);
        mDiscoveryBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.to_home_btn:
                ARouter.getInstance()
                        .build(HomeRouterConstants.HOME_ACTIVITY)
                        .withString(AppConstants.ARG1, "home....")
                        .navigation();
                break;
            case R.id.to_discovery_btn:
                ARouter.getInstance()
                        .build(DiscoveryRouterConstants.DISCOVERY_ACTIVITY)
                        .withString(AppConstants.ARG1, "discovery....")
                        .navigation();
                break;
            default:
                break;
        }
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