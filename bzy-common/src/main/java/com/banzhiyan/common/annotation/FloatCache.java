package com.banzhiyan.common.annotation;

/**
 * 用于对并发度高的接口方法，可实现一个请求之后定时将内容缓存，其他请求过来时直接返回缓存内容，一定时间内没有请求时，会停止更新缓存
 *
 * Created by xn025665 on 2017/8/22
 */

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface FloatCache {
    // 间隔多久执行
    long period() default 3;
    // 时间单位
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    String cacheName() default "";

    String cacheKey() default "";

    // 延迟缓存多久（单位秒）
    int floatTime() default 30;
}
