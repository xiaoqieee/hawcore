package com.banzhiyan.cache.pointcut;

import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by xn025665 on 2017/8/24.
 */

public class CachePointcuts {
    public CachePointcuts() {
    }

    @Pointcut("execution(@ooh.bravo.cache.annotation.Cacheable * *(..))")
    public void cacheable() {
    }

    @Pointcut("execution(@ooh.bravo.cache.annotation.CacheEvict * *(..))")
    public void cacheEvict() {
    }

    @Pointcut("execution(@ooh.bravo.cache.annotation.CacheFlush * *(..))")
    public void cacheFlush() {
    }
}

