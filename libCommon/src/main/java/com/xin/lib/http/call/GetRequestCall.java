package com.xin.lib.http.call;

import java.util.Map;

import okhttp3.Request;

/**
 * <br> ClassName:    GetRequestCall
 * <br> Description:  Get 请求
 * <br>
 * <br> @author:      zouxinjie
 * <br> Date:         2020/8/21 14:51
 */
public class GetRequestCall extends RequestCall {
    
    /**
     *<br> Description: Get请求
     *<br> Author:      zouxinjie
     *<br> Date:        2020/9/18 16:41
     *
     * @param url String
     */
    public GetRequestCall(String url) {
        super(url);
    }

    /**
     *<br> Description: 增加 GET 请求参数 - long
     *<br> Author:      zouxinjie
     *<br> Date:        2020/10/29 11:40
     * @deprecated Use addUrlParams
     */
    @Deprecated
    public GetRequestCall addParams(String key, long value) {
        addUrlParams(key, String.valueOf(value));
        return this;
    }

    /**
     *<br> Description: 增加 GET 请求参数 - int
     *<br> Author:      zouxinjie
     *<br> Date:        2020/10/29 11:40
     * @deprecated Use addUrlParams
     */
    @Deprecated
    public GetRequestCall addParams(String key, int value) {
        addUrlParams(key, String.valueOf(value));
        return this;
    }

    /**
     *<br> Description: 增加 GET 请求参数 - String
     *<br> Author:      zouxinjie
     *<br> Date:        2020/10/29 11:40
     * @deprecated Use addUrlParams
     */
    @Deprecated
    public GetRequestCall addParams(String key, String value) {
        addUrlParams(key, value);
        return this;
    }

    /**
     *<br> Description: 批量增加 GET 请求参数 -
     *<br> Author:      zouxinjie
     *<br> Date:        2020/10/29 11:40
     * @deprecated Use addUrlParams
     */
    @Deprecated
    public GetRequestCall addParams(Map<String, String> paramsMap) {
        addUrlParams(paramsMap);
        return this;
    }


    @Override
    public Request generateRequest() {
        return new Request.Builder()
                .url(getGenerateUrl())
                .build();
    }


}
