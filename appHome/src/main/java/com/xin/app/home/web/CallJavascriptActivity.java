package com.xin.app.home.web;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xin.app.home.R;
import com.xin.lib.base.AbstractActivity;
import com.xin.lib.dbridge.utils.OnReturnValue;
import com.xin.lib.dbridge.webview.DWebView;

import org.json.JSONObject;

public class CallJavascriptActivity extends AbstractActivity implements View.OnClickListener {

    private DWebView mXWebView;

    private Button addValue;
    private Button append;
    private Button startTimer;
    private Button synAddValue;
    private Button synGetInfo;
    private Button asynAddValue;
    private Button asynGetInfo;
    private Button hasMethodAddValue;
    private Button hasMethodXX;
    private Button hasMethodAsynAddValue;
    private Button hasMethodAsynXX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_call_javascript);
        addValue = findViewById(R.id.js_add_value);
        append = findViewById(R.id.append);
        startTimer = findViewById(R.id.startTimer);
        synAddValue = findViewById(R.id.synAddValue);
        synGetInfo = findViewById(R.id.synGetInfo);
        asynAddValue = findViewById(R.id.asynAddValue);
        asynGetInfo = findViewById(R.id.asynGetInfo);
        hasMethodAddValue = findViewById(R.id.hasMethodAddValue);
        hasMethodXX = findViewById(R.id.hasMethodXX);
        hasMethodAsynAddValue = findViewById(R.id.hasMethodAsynAddValue);
        hasMethodAsynXX = findViewById(R.id.hasMethodAsynXX);
        addValue.setOnClickListener(this);
        append.setOnClickListener(this);
        startTimer.setOnClickListener(this);
        synAddValue.setOnClickListener(this);
        synGetInfo.setOnClickListener(this);
        asynAddValue.setOnClickListener(this);
        asynGetInfo.setOnClickListener(this);
        hasMethodAddValue.setOnClickListener(this);
        hasMethodXX.setOnClickListener(this);
        hasMethodAsynAddValue.setOnClickListener(this);
        hasMethodAsynXX.setOnClickListener(this);

        mXWebView = findViewById(R.id.x_webview_test);
        mXWebView.setWebContentsDebuggingEnabled(true);
        mXWebView.loadUrl("file:///android_asset/native-call-js.html");
    }

    private void showToast(Object o) {
        if (o != null) {
            Toast.makeText(this, o.toString(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "o is null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.js_add_value) {
            mXWebView.callHandler("addValue", new Object[]{3, 4}, new OnReturnValue<Integer>() {
                @Override
                public void onValue(Integer retValue) {
                    showToast(retValue);
                }
            });
        } else if (id == R.id.append) {
            mXWebView.callHandler("append", new Object[]{"I", "love", "you"}, new OnReturnValue<String>() {
                @Override
                public void onValue(String retValue) {
                    showToast(retValue);
                }
            });
        } else if (id == R.id.startTimer) {
            mXWebView.callHandler("startTimer", new OnReturnValue<Integer>() {
                @Override
                public void onValue(Integer retValue) {
                    showToast(retValue);
                }
            });
        } else if (id == R.id.synAddValue) {
            mXWebView.callHandler("syn.addValue", new Object[]{5, 6}, new OnReturnValue<Integer>() {
                @Override
                public void onValue(Integer retValue) {
                    showToast(retValue);
                }
            });
        } else if (id == R.id.synGetInfo) {
            mXWebView.callHandler("syn.getInfo", new OnReturnValue<JSONObject>() {
                @Override
                public void onValue(JSONObject retValue) {
                    showToast(retValue);
                }
            });
        } else if (id == R.id.asynAddValue) {
            mXWebView.callHandler("asyn.addValue", new Object[]{5, 6}, new OnReturnValue<Integer>() {
                @Override
                public void onValue(Integer retValue) {
                    showToast(retValue);
                }
            });
        } else if (id == R.id.asynGetInfo) {
            mXWebView.callHandler("asyn.getInfo", new OnReturnValue<JSONObject>() {
                @Override
                public void onValue(JSONObject retValue) {
                    showToast(retValue);
                }
            });
        } else if (id == R.id.hasMethodAddValue) {
            mXWebView.hasJavascriptMethod("addValue", new OnReturnValue<Boolean>() {
                @Override
                public void onValue(Boolean retValue) {
                    showToast(retValue);
                }
            });
        } else if (id == R.id.hasMethodXX) {
            mXWebView.hasJavascriptMethod("XX", new OnReturnValue<Boolean>() {
                @Override
                public void onValue(Boolean retValue) {
                    showToast(retValue);
                }
            });
        } else if (id == R.id.hasMethodAsynAddValue) {
            mXWebView.hasJavascriptMethod("asyn.addValue", new OnReturnValue<Boolean>() {
                @Override
                public void onValue(Boolean retValue) {
                    showToast(retValue);
                }
            });
        } else if (id == R.id.hasMethodAsynXX) {
            mXWebView.hasJavascriptMethod("asyn.XX", new OnReturnValue<Boolean>() {
                @Override
                public void onValue(Boolean retValue) {
                    showToast(retValue);
                }
            });
        }

    }
}
