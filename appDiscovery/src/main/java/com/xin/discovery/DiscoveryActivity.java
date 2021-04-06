package com.xin.discovery;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.xin.appdiscovery.R;
import com.xin.lib.arouter.DiscoveryRouterConstants;
import com.xin.lib.constants.AppConstants;

@Route(path = DiscoveryRouterConstants.DISCOVERY_ACTIVITY)
public class DiscoveryActivity extends AppCompatActivity {

    @Autowired(name = AppConstants.ARG1)
    String mDiscoveryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discovery_activity);

        TextView titleView = findViewById(R.id.discovery_text);
        titleView.setText(mDiscoveryText);
    }
}