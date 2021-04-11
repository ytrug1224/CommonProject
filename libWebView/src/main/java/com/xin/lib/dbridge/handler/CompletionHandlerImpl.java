package com.xin.lib.dbridge.handler;

/**
 *  ClassName:    CompletionHandlerImpl
 *  Description:  原生调用返回处理handler
 *
 *  @author:      zouxinjie
 * Date:         2019/10/25 10:53
 */
public abstract class CompletionHandlerImpl implements CompletionHandler {
    private String callBackStr;

    public String getCallBackStr() {
        return callBackStr;
    }

    public CompletionHandlerImpl(String callBackStr) {
        this.callBackStr = callBackStr;
    }
}