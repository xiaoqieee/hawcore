package com.banzhiyan.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.TimeZone;

/**
 * Created by xn025665 on 2017/8/24.
 */

public class JsonUtils {
    private static final ObjectMapper JACKSON = new ObjectMapper();
    private static final Gson GSON;

    public JsonUtils() {
    }

    public static <T> String toJSON(T object) {
        try {
            return JACKSON.writeValueAsString(object);
        } catch (Exception var2) {
            return GSON.toJson(object);
        }
    }

    public static <T> T parseJSON(String json, Class<T> clazz) {
        try {
            return JACKSON.readValue(json, clazz);
        } catch (Exception var3) {
            return GSON.fromJson(json, clazz);
        }
    }

    /** @deprecated */
    @Deprecated
    public static <T> T parseJSON(String json, Class<?> parametrized, Class... parameterClass) {
        try {
            JavaType type = JACKSON.getTypeFactory().constructParametrizedType(parametrized, parametrized, parameterClass);
            return JACKSON.readValue(json, type);
        } catch (Exception var4) {
            return null;
        }
    }

    static {
        JACKSON.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        VisibilityChecker<?> visibilityChecker = JACKSON.getSerializationConfig().getDefaultVisibilityChecker();
        JACKSON.setVisibility(visibilityChecker.withFieldVisibility(JsonAutoDetect.Visibility.ANY).withCreatorVisibility(JsonAutoDetect.Visibility.NONE).withGetterVisibility(JsonAutoDetect.Visibility.NONE).withSetterVisibility(JsonAutoDetect.Visibility.NONE).withIsGetterVisibility(JsonAutoDetect.Visibility.NONE));
        JACKSON.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        JACKSON.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        JACKSON.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        GSON = (new GsonBuilder()).create();
    }
}

