package com.xin.lib.http.converter;

import android.util.Log;

import okhttp3.Call;
import okhttp3.Response;

/**
 * <br> ClassName:    StringConverter
 * <br> Description:
 * <br>
 * <br> @author:      zouxinjie
 * <br> Date:         2020/8/21 15:44
 */
public class StringConverter implements IConverter<String> {

    @Override
    public String parseResponse(Call call, Response response) throws Exception {
        String result = response.body().string();
        Log.d("smart", "StringConverter:" + result);

        return result;
    }

}
