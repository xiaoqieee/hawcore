package com.banzhiyan.tolerance.annotation;

/**
 * Created by xn025665 on 2017/8/24.
 */

import com.banzhiyan.cache.constant.CacheType;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 用于高并发的查询接口，将限流和缓存合并，允许的线程从数据库读取数据并保存在缓存中，而被限制的线程从缓存中获取数据
 * Created by xn025665 on 2017/6/18.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RequestExecLimiter {
    // 允许并发量
    int concurrency() default 15;
    // 并发超时时间
    int concurrentTimeout() default 0;
    // 是否快速失败
    boolean failfast() default false;
    // 是否安全失败
    boolean failsafe() default true;
    // 使用的缓存类型
    CacheType cacheType() default CacheType.REDIS;
    // 缓存名称
    String cacheName() default "";
    // 缓存hash结构的key
    String cacheKey() default "";
    // 缓存TTL(cacheType为MEMORY时此属性无效，此时默认也是3秒，不可修改)
    long cacheTimeout() default 3l;
    // 缓存TTL单位
    TimeUnit cacheTimeUnit() default TimeUnit.SECONDS;
    // 从缓存中最多获取尝试次数，每一次会间隔50微秒
    int tryTimes() default 50;
}

