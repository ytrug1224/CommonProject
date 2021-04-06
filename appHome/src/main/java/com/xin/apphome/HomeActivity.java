package com.xin.apphome;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.xin.lib.arouter.HomeRouterConstants;
import com.xin.lib.constants.AppConstants;

@Route(path = HomeRouterConstants.HOME_ACTIVITY)
public class HomeActivity extends AppCompatActivity {

    @Autowired(name = AppConstants.ARG1)
    String mHomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        TextView titleView = findViewById(R.id.home_text);
        titleView.setText(mHomeText);
    }
}