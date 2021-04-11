package com.xin.lib.dbridge.handler;

public interface CompletionHandler<T> {
    void complete(T retValue);
    void complete();
    void setProgressData(T value);
}