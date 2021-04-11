package com.xin.lib.dbridge.handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class WebCallInfo {
    public String data;
    public int callbackId;
    public String method;

    public WebCallInfo(String handlerName, int id, Object[] args) {
        if (args == null) {
            args = new Object[0];
        }
        data = new JSONArray(Arrays.asList(args)).toString();
        callbackId = id;
        method = handlerName;
    }

    @Override
    public String toString() {
        JSONObject jo = new JSONObject();
        try {
            jo.put("method", method);
            jo.put("callbackId", callbackId);
            jo.put("data", data);
        } catch (JSONException e) {
            // do nothing
        }
        return jo.toString();
    }
}