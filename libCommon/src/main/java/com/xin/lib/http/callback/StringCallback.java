package com.xin.lib.http.callback;

import android.util.Log;

import okhttp3.Call;
import okhttp3.Response;

/**
 * <br> ClassName:    StringCallback
 * <br> Description:  一般字符串回调处理
 * <br>
 * <br> @author:      zouxinjie
 * <br> Date:         2020/8/21 15:43
 */
public abstract class StringCallback extends AbstractCallback<String> {

    @Override
    public String parseResponse(Call call, Response response) throws Exception {
        String result = response.body().string();
        Log.d("vsmart", "StringCallback:" + result);

        return result;
    }


}
