package com.banzhiyan.cache.annotation;

import com.banzhiyan.cache.constant.CacheType;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by xn025665 on 2017/8/24.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface Cacheable {
    CacheType cacheType() default CacheType.REDIS;

    String cacheName();

    String key() default "";

    long timeout() default 0L;

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
