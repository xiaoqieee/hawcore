package com.banzhiyan.cache.annotation;

import com.banzhiyan.cache.constant.CacheType;

import java.lang.annotation.*;

/**
 * Created by xn025665 on 2017/8/24.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface CacheFlush {
    CacheType cacheType() default CacheType.REDIS;

    String cacheName();
}
