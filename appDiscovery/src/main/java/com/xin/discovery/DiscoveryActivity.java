package com.xin.discovery;

import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.xin.appdiscovery.R;
import com.xin.lib.arouter.DiscoveryRouterConstants;
import com.xin.lib.constants.AppConstants;
import com.xin.lib.mvp.base.AbstractMvpActivity;
import com.xin.lib.mvp.base.AbstractMvpPresenter;

@Route(path = DiscoveryRouterConstants.DISCOVERY_ACTIVITY)
public class DiscoveryActivity extends AbstractMvpActivity {

    @Autowired(name = AppConstants.ARG1)
    String mDiscoveryText;

    @Override
    protected AbstractMvpPresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discovery_activity);

        TextView titleView = findViewById(R.id.discovery_text);
        titleView.setText(mDiscoveryText);
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