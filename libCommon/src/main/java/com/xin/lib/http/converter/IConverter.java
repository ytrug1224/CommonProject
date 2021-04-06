package com.xin.lib.http.converter;

import okhttp3.Call;
import okhttp3.Response;

/**
 * <br> ClassName:    IConverter
 * <br> Description:
 * <br>
 * <br> @author:      zouxinjie
 * <br> Date:         2020/8/21 15:45
 */
public interface IConverter<T> {

    /**
     *<br> Description: 数据解析（子线程）
     *<br> Author:      zouxinjie
     *<br> Date:        2020/6/13 15:17
     *
     * @param call Call
     * @param response Response
     * @return T
     * @throws Exception e
     */
    T parseResponse(Call call, Response response) throws Exception;


}
