package com.banzhiyan.util;

import org.springframework.util.*;

import java.lang.reflect.Field;

/**
 * Created by xn025665 on 2017/8/23.
 */

public final class FieldUtils {
    public FieldUtils() {
    }

    public static Field getField(Class<?> clazz, String fieldName) throws IllegalStateException {
        Assert.notNull(clazz, "Class required");
        Assert.hasText(fieldName, "Field name required");

        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException var3) {
            if(clazz.getSuperclass() != null) {
                return getField(clazz.getSuperclass(), fieldName);
            } else {
                throw new IllegalStateException("Could not locate field '" + fieldName + "' on class " + clazz);
            }
        }
    }

    public static Object getFieldValue(Object bean, String fieldName) throws IllegalAccessException {
        Assert.notNull(bean, "Bean cannot be null");
        Assert.hasText(fieldName, "Field name required");
        String[] nestedFields = org.springframework.util.StringUtils.tokenizeToStringArray(fieldName, ".");
        Class<?> componentClass = bean.getClass();
        Object value = bean;
        String[] arr$ = nestedFields;
        int len$ = nestedFields.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String nestedField = arr$[i$];
            Field field = getField(componentClass, nestedField);
            field.setAccessible(true);
            value = field.get(value);
            if(value != null) {
                componentClass = value.getClass();
            }
        }

        return value;
    }

    public static Object getProtectedFieldValue(String protectedField, Object object) {
        Field field = getField(object.getClass(), protectedField);

        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception var4) {
            ReflectionUtils.handleReflectionException(var4);
            return null;
        }
    }

    public static void setProtectedFieldValue(String protectedField, Object object, Object newValue) {
        Field field = getField(object.getClass(), protectedField);

        try {
            field.setAccessible(true);
            field.set(object, newValue);
        } catch (Exception var5) {
            ReflectionUtils.handleReflectionException(var5);
        }

    }
}

