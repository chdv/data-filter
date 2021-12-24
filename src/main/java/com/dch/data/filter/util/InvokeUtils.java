package com.dch.data.filter.util;

import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

public class InvokeUtils {

    private InvokeUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> T invokeGetter(Object bean, String field) {
        try {
            Method method = bean.getClass().getMethod("get" + StringUtils.capitalize(field));
            return (T) method.invoke(bean);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}
