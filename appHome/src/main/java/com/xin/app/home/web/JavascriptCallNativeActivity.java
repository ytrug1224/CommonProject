package com.xin.app.home.web;

import android.os.Bundle;

import com.xin.app.home.R;
import com.xin.app.home.web.jsinterface.JsApi;
import com.xin.app.home.web.jsinterface.JsEchoApi;
import com.xin.lib.base.AbstractActivity;
import com.xin.lib.dbridge.webview.DWebView;

public class JavascriptCallNativeActivity extends AbstractActivity {

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
}
