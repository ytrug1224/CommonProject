package com.xin.lib.http.call;

/**
 * <br> ClassName:    CallAdapter
 * <br> Description:  兼容RxJava扩展- vivo_it_http_extend
 * <br>
 * <br> @author:      zouxinjie
 * <br> Date:         2020/8/22 8:59
 */
public interface CallAdapter<T> {

    T adapt(RequestCall requestCall);

}
