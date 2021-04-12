package com.xin.app.home.web;

import android.os.Bundle;

import com.xin.app.home.R;
import com.xin.app.home.web.jsinterface.JsApi;
import com.xin.app.home.web.jsinterface.JsEchoApi;
import com.xin.lib.dbridge.webview.DWebView;
import com.xin.lib.mvp.base.AbstractMvpActivity;
import com.xin.lib.mvp.base.AbstractMvpPresenter;

public class JavascriptCallNativeActivity extends AbstractMvpActivity {

    @Override
    protected AbstractMvpPresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_js_call_native);

        DWebView webView = findViewById(R.id.dWebview);
        // set debug mode
        webView.setWebContentsDebuggingEnabled(true);
        webView.loadUrl("file:///android_asset/js-call-native.html");
        webView.addJavascriptObject(new JsApi(this), null);
        webView.addJavascriptObject(new JsEchoApi(this), "echo");
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
