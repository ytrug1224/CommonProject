package com.xin.lib.dbridge.webview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.Keep;

import com.xin.lib.dbridge.handler.CompletionHandler;
import com.xin.lib.dbridge.handler.CompletionHandlerImpl;
import com.xin.lib.dbridge.handler.WebCallInfo;
import com.xin.lib.dbridge.listener.FileChooser;
import com.xin.lib.dbridge.listener.JavascriptCloseWindowListener;
import com.xin.lib.dbridge.utils.CommonUtils;
import com.xin.lib.dbridge.utils.OnReturnValue;
import com.xin.lib.log.Logger;
import com.xin.lib.widget.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *  ClassName:    DWebView
 *  Description:  基于原生WebView的封装
 *
 *  @author:      zouxinjie
 * Date:         2019/10/26 15:20
 */
public class DWebView extends WebView {
    private static final String BRIDGE_NAME = "_dsbridge";
    private static final String LOG_TAG = "dsBridge";
    private static boolean mIsDebug = false;
    private String APP_CACHE_DIRNAME;
    private int mCallId = 0;
    private WebChromeClient gWebChromeClient;

    private volatile boolean alertBoxBlock = true;
    private JavascriptCloseWindowListener mJsCloseWindowListener = null;
    private Map<String, Object> mJsNamespaceInterfaces = new HashMap<String, Object>();
    private ArrayList<WebCallInfo> mWebCallInfoList;
    private InnerJavascriptInterface mInnerJsInterface = new InnerJavascriptInterface();

    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private Map<Integer, OnReturnValue> handlerMap = new HashMap<>();

    public DWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DWebView(Context context) {
        super(context);
        init();
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void init() {
        APP_CACHE_DIRNAME = getContext().getFilesDir().getAbsolutePath() + "/webcache";
        WebSettings settings = getSettings();
        settings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setAllowFileAccess(false);
        settings.setAppCacheEnabled(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAppCachePath(APP_CACHE_DIRNAME);
        settings.setUseWideViewPort(true);
        super.setWebChromeClient(mWebChromeClient);
        addInternalJavascriptObject();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            super.addJavascriptInterface(mInnerJsInterface, BRIDGE_NAME);
        } else {
            // add dsbridge tag in lower android version
            settings.setUserAgentString(settings.getUserAgentString() + " _dsbridge");
        }
    }


    private String[] parseNamespace(String method) {
        int pos = method.lastIndexOf('.');
        String namespace = "";
        if (pos != -1) {
            namespace = method.substring(0, pos);
            method = method.substring(pos + 1);
        }
        return new String[]{namespace, method};
    }

    @Keep
    private void addInternalJavascriptObject() {
        addJavascriptObject(new Object() {

            @Keep
            @JavascriptInterface
            public boolean hasNativeMethod(Object args) throws JSONException {
                JSONObject jsonObject = (JSONObject) args;
                String methodName = jsonObject.getString("name").trim();
                String type = jsonObject.getString("type").trim();
                String[] nameStr = parseNamespace(methodName);
                Object jsb = mJsNamespaceInterfaces.get(nameStr[0]);
                if (jsb != null) {
                    Class<?> cls = jsb.getClass();
                    boolean asyn = false;
                    Method method = null;
                    try {
                        method = cls.getMethod(nameStr[1],
                                new Class[]{Object.class, CompletionHandler.class});
                        asyn = true;
                    } catch (Exception e) {
                        try {
                            method = cls.getMethod(nameStr[1], new Class[]{Object.class});
                        } catch (Exception ex) {

                        }
                    }
                    if (method != null) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            JavascriptInterface annotation = method.getAnnotation(JavascriptInterface.class);
                            if (annotation == null) {
                                return false;
                            }
                        }
                        if ("all".equals(type) || (asyn && "asyn".equals(type) || (!asyn && "syn".equals(type)))) {
                            return true;
                        }

                    }
                }
                return false;
            }

            @Keep
            @JavascriptInterface
            public String closePage(Object object) throws JSONException {
                runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mJsCloseWindowListener == null
                                || mJsCloseWindowListener.onClose()) {
                            Context context = getContext();
                            if (context instanceof Activity) {
                                ((Activity)context).onBackPressed();
                            }
                        }
                    }
                });
                return null;
            }

            @Keep
            @JavascriptInterface
            public void disableJavascriptDialogBlock(Object object) throws JSONException {
                JSONObject jsonObject = (JSONObject) object;
                alertBoxBlock = !jsonObject.getBoolean("disable");
            }

            @Keep
            @JavascriptInterface
            public void dsinit(Object jsonObject) {
                DWebView.this.dispatchStartupQueue();
            }

            @Keep
            @JavascriptInterface
            public void returnValue(final Object obj){
                runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = (JSONObject) obj;
                        Object data = null;
                        try {
                            int id = jsonObject.getInt("id");
                            boolean isCompleted = jsonObject.getBoolean("complete");
                            OnReturnValue handler = handlerMap.get(id);
                            if (jsonObject.has("data")) {
                                data = jsonObject.get("data");
                            }
                            if (handler != null) {
                                handler.onValue(data);
                                if (isCompleted) {
                                    handlerMap.remove(id);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        }, "_dsb");
    }

    private void _evaluateJavascript(String script) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            DWebView.super.evaluateJavascript(script, null);
        } else {
            super.loadUrl("javascript:" + script);
        }
    }

    /**
     * This method can be called in any thread, and if it is not called in the main thread,
     * it will be automatically distributed to the main thread.
     *
     * @param script
     */
    public void evaluateJavascript(final String script) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                _evaluateJavascript(script);
            }
        });
    }

    /**
     * This method can be called in any thread, and if it is not called in the main thread,
     * it will be automatically distributed to the main thread.
     *
     * @param url
     */
    @Override
    public void loadUrl(final String url) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (url != null && url.startsWith("javascript:")){
                    DWebView.super.loadUrl(url);
                }else{
                    mWebCallInfoList = new ArrayList<>();
                    DWebView.super.loadUrl(url);
                }
            }
        });
    }

    /**
     * This method can be called in any thread, and if it is not called in the main thread,
     * it will be automatically distributed to the main thread.
     *
     * @param url
     * @param additionalHttpHeaders
     */
    @Override
    public void loadUrl(final String url, final Map<String, String> additionalHttpHeaders) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (url != null && url.startsWith("javascript:")){
                    DWebView.super.loadUrl(url, additionalHttpHeaders);
                }else{
                    mWebCallInfoList = new ArrayList<>();
                    DWebView.super.loadUrl(url, additionalHttpHeaders);
                }
            }
        });
    }

    @Override
    public void reload() {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                mWebCallInfoList = new ArrayList<>();
                DWebView.super.reload();
            }
        });
    }

    /**
     * set a listener for javascript closing the current activity.
     */
    public void setJsCloseWindowListener(JavascriptCloseWindowListener listener) {
        mJsCloseWindowListener = listener;
    }

    private synchronized void dispatchStartupQueue() {
        if (mWebCallInfoList != null) {
            for (WebCallInfo info : mWebCallInfoList) {
                dispatchJavascriptCall(info);
            }
            mWebCallInfoList = null;
        }
    }

    private void dispatchJavascriptCall(WebCallInfo info) {
        evaluateJavascript(String.format("window._handleMessageFromNative(%s)", info.toString()));
    }

    public synchronized <T> void callHandler(String method, Object[] args, final OnReturnValue<T> handler) {
        WebCallInfo webCallInfo = new WebCallInfo(method, mCallId++, args);
        if (handler != null) {
            handlerMap.put(webCallInfo.callbackId, handler);
        }

        if (mWebCallInfoList != null) {
            mWebCallInfoList.add(webCallInfo);
        } else {
            dispatchJavascriptCall(webCallInfo);
        }

    }

    public void callHandler(String method, Object[] args) {
        callHandler(method, args, null);
    }

    public <T> void callHandler(String method, OnReturnValue<T> handler) {
        callHandler(method, null, handler);
    }

    /**
     * Test whether the handler exist in javascript
     *
     * @param handlerName
     * @param existCallback
     */
    public void hasJavascriptMethod(String handlerName, OnReturnValue<Boolean> existCallback) {
        callHandler("_hasJavascriptMethod", new Object[]{handlerName}, existCallback);
    }

    /**
     * Add a java object which implemented the javascript interfaces to dsBridge with namespace.
     * Remove the object using {@link #removeJavascriptObject(String) removeJavascriptObject(String)}
     *
     * @param object
     * @param namespace if empty, the object have no namespace.
     */
    public void addJavascriptObject(Object object, String namespace) {
        if (namespace == null) {
            namespace = "";
        }
        if (object != null) {
            mJsNamespaceInterfaces.put(namespace, object);
        }
    }

    public void removeJavascriptObject(String namespace) {
        if (namespace == null) {
            namespace = "";
        }
        mJsNamespaceInterfaces.remove(namespace);
    }

    public void disableJavascriptDialogBlock(boolean disable) {
        alertBoxBlock = !disable;
    }

    @Override
    public void setWebChromeClient(WebChromeClient client) {
        gWebChromeClient = client;
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient() {

        @Override
        public boolean onJsAlert(WebView view, String url, final String message, final JsResult result) {
            if (!alertBoxBlock) {
                result.confirm();
            }
            if (gWebChromeClient != null) {
                if (gWebChromeClient.onJsAlert(view, url, message, result)) {
                    return true;
                }
            }
            CustomDialog alertDialog = new CustomDialog(getContext());
            alertDialog.setContent(message);
            alertDialog.setCancelable(false);
            alertDialog.setConfirmButton(new CustomDialog.OnClickListener() {
                @Override
                public boolean onClick(CustomDialog dialog) {
                    if (alertBoxBlock) {
                        result.confirm();
                    }
                    return false;
                }
            });
            alertDialog.setCancelButton(new CustomDialog.OnClickListener() {
                @Override
                public boolean onClick(CustomDialog dialog) {
                    dialog.cancel();
                    return false;
                }
            });
            alertDialog.show();
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            if (!alertBoxBlock) {
                result.confirm();
            }
            if (gWebChromeClient != null && gWebChromeClient.onJsConfirm(view, url, message, result)) {
                return true;
            } else {
                CustomDialog.OnClickListener listener = new CustomDialog.OnClickListener() {
                    @Override
                    public boolean onClick(CustomDialog dialog) {
                        if (alertBoxBlock) {
                            result.confirm();
                        }
                        return false;
                    }

                };
                CustomDialog customDialog = new CustomDialog(getContext());
                customDialog.setContent(message);
                customDialog.setCancelable(false);
                customDialog.setConfirmButton(new CustomDialog.OnClickListener() {
                    @Override
                    public boolean onClick(CustomDialog dialog) {
                        return false;
                    }
                });
                customDialog.setCancelButton(new CustomDialog.OnClickListener() {
                    @Override
                    public boolean onClick(CustomDialog dialog) {
                        result.cancel();
                        dialog.cancel();
                        return false;
                    }
                });
                customDialog.show();
                return true;
            }
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, final String message,
                                  String defaultValue, final JsPromptResult result) {

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                String prefix = "_dsbridge=";
                if (message.startsWith(prefix)) {
                    result.confirm(mInnerJsInterface.call(message.substring(prefix.length()), defaultValue));
                    return true;
                }
            }

            if (!alertBoxBlock) {
                result.confirm();
            }

            if (gWebChromeClient != null && gWebChromeClient.onJsPrompt(view, url, message, defaultValue, result)) {
                return true;
            } else {
                CustomDialog.OnClickListener listener = new CustomDialog.OnClickListener() {
                    @Override
                    public boolean onClick(CustomDialog dialog) {
                        if (alertBoxBlock) {
                            result.confirm(dialog.getEditTextStr());
                        }
                        return false;
                    }
                };
                CustomDialog customDialog = new CustomDialog(getContext());
                customDialog.setTitle(message);
                customDialog.setEnableInput(true);
                customDialog.setInputText("", defaultValue);
                customDialog.setCancelable(false);
                customDialog.setConfirmButton(listener);
                customDialog.setCancelButton(new CustomDialog.OnClickListener() {
                    @Override
                    public boolean onClick(CustomDialog dialog) {
                        result.cancel();
                        dialog.cancel();
                        return false;
                    }
                });
                customDialog.show();
                return true;
            }
        }

        @Keep
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public void openFileChooser(ValueCallback valueCallback, String acceptType) {
            if (gWebChromeClient instanceof FileChooser) {
                ((FileChooser) gWebChromeClient).openFileChooser(valueCallback, acceptType);
            }
        }

        @Keep
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
            if (gWebChromeClient instanceof FileChooser) {
                ((FileChooser) gWebChromeClient).openFileChooser(valueCallback, acceptType, capture);
            }
        }
    };

    @Override
    public void clearCache(boolean includeDiskFiles) {
        super.clearCache(includeDiskFiles);
        CookieManager.getInstance().removeAllCookie();
        Context context = getContext();
        try {
            context.deleteDatabase("webview.db");
            context.deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        File appCacheDir = new File(APP_CACHE_DIRNAME);
        File webviewCacheDir = new File(context.getCacheDir().getAbsolutePath() + "/webviewCache");

        if (webviewCacheDir.exists()) {
            CommonUtils.deleteFile(webviewCacheDir);
        }

        if (appCacheDir.exists()) {
            CommonUtils.deleteFile(appCacheDir);
        }
    }

    private void runOnMainThread(Runnable runnable) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            runnable.run();
            return;
        }
        mainHandler.post(runnable);
    }

    private class InnerJavascriptInterface {

        @Keep
        @JavascriptInterface
        public String call(String methodName, String argStr) {
            String error = "Js bridge  called, but can't find a corresponded JavascriptInterface object , please check your code!";
            String[] nameStr = parseNamespace(methodName.trim());
            methodName = nameStr[1];
            Object jsb = mJsNamespaceInterfaces.get(nameStr[0]);
            JSONObject ret = new JSONObject();
            try {
                ret.put("code", -1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (jsb == null) {
                printDebugInfo(error);
                return ret.toString();
            }
            Object arg=null;
            Method method = null;
            String callback = null;

            try {
                JSONObject args = new JSONObject(argStr);
                if (args.has("_dscbstub")) {
                    callback = args.getString("_dscbstub");
                }
                if(args.has("data")) {
                    arg = args.get("data");
                }
            } catch (JSONException e) {
                error = String.format("The argument of \"%s\" must be a JSON object string!", methodName);
                printDebugInfo(error);
                e.printStackTrace();
                return ret.toString();
            }

            Class<?> cls = jsb.getClass();
            boolean asyn = false;
            try {
                method = cls.getMethod(methodName, new Class[]{Object.class, CompletionHandler.class});
                asyn = true;
            } catch (Exception e) {
                try {
                    method = cls.getMethod(methodName, new Class[]{Object.class});
                } catch (Exception ex) {

                }
            }

            if (method == null) {
                error = "Not find method \"" + methodName + "\" implementation! please check if the  signature or namespace of the method is right ";
                printDebugInfo(error);
                return ret.toString();
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                JavascriptInterface annotation = method.getAnnotation(JavascriptInterface.class);
                if (annotation == null) {
                    error = "Method " + methodName + " is not invoked, since it is not declared with JavascriptInterface annotation! ";
                    printDebugInfo(error);
                    return ret.toString();
                }
            }

            Object retData;
            method.setAccessible(true);
            try {
                if (asyn) {
                    method.invoke(jsb, arg, new CompletionHandlerImpl(callback) {

                        @Override
                        public void complete(Object retValue) {
                            complete(retValue, true);
                        }

                        @Override
                        public void complete() {
                            complete(null, true);
                        }

                        @Override
                        public void setProgressData(Object value) {
                            complete(value, false);
                        }

                        private void complete(Object retValue, boolean complete) {
                            try {
                                JSONObject ret = new JSONObject();
                                ret.put("code", 0);
                                ret.put("data", retValue);
                                if (getCallBackStr() != null) {
                                    String script = String.format("%s(%s.data);", getCallBackStr(), ret.toString());
                                    if (complete) {
                                        script += "delete window." + getCallBackStr();
                                    }
                                    evaluateJavascript(script);
                                }
                            } catch (Exception e) {
                                Logger.e("complete ->>", e.toString());
                            }
                        }
                    });
                } else {
                    retData = method.invoke(jsb, arg);
                    ret.put("code", 0);
                    ret.put("data", retData);
                    return ret.toString();
                }
            } catch (Exception e) {
                error = String.format("Call failed：The parameter of \"%s\" in Java is invalid.", methodName);
                printDebugInfo(error);
                return ret.toString();
            }
            return ret.toString();
        }

        private void printDebugInfo(String error) {
            Log.d(LOG_TAG, error);
            if (mIsDebug) {
                evaluateJavascript(String.format("alert('%s')", "DEBUG ERR MSG:\\n" + error.replaceAll("\\'", "\\\\'")));
            }
        }
    }
}