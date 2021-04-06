package com.xin.lib.http.converter;

import java.lang.reflect.Type;

/**
 * <br> ClassName:    SmartConverter
 * <br> Description:
 * <br>
 * <br> @author:      zouxinjie
 * <br> Date:         2020/8/21 15:45
 */
public abstract class SmartConverter<T> implements IConverter<T> {

    protected Type dataType;

    public SmartConverter(Type type) {
        this.dataType = type;
    }

    public void setType(Type type) {
        this.dataType = type;
    }

}
