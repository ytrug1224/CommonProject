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
import com.xin.lib.base.AbstractActivity;
import com.xin.lib.constants.AppConstants;

@Route(path = HomeRouterConstants.HOME_ACTIVITY)
public class HomeActivity extends AbstractActivity {

    @Autowired(name = AppConstants.ARG1)
    String mHomeText;

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
}