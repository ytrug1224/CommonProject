package com.xin.app.home.web.jsinterface;

import android.app.Activity;
import android.webkit.JavascriptInterface;

import com.xin.lib.dbridge.handler.CompletionHandler;
import com.xin.lib.log.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class JsEchoApi {

    private Activity mActivity;

    public JsEchoApi(Activity activity) {
        this.mActivity = activity;
    }

    @JavascriptInterface
    public Object syn(Object args) throws JSONException {
        Logger.w("zxj syn ->> ", String.valueOf(args));
        JSONObject jsonObject = new JSONObject(String.valueOf(args));
        return jsonObject.get("msg");
    }

    @JavascriptInterface
    public void asyn(Object args, CompletionHandler handler) {
        handler.complete(args);
    }

    @JavascriptInterface
    public String testWeb(Object args) {
        return String.valueOf(args);
    }

    @JavascriptInterface
    public void testExit(Object args) {
        Logger.w("zxj ->> ", String.valueOf(args));
        mActivity.finish();
    }
}