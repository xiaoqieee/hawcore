package com.banzhiyan.cache.mencache;

import com.google.common.cache.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by xn025665 on 2017/8/24.
 */

public class LocalMemCache {

    private final static Cache<String,Object> memCache
            //CacheBuilder的构造函数是私有的，只能通过其静态方法newBuilder()来获得CacheBuilder的实例
            = CacheBuilder.newBuilder()
            //设置并发级别为8，并发级别是指可以同时写缓存的线程数
            .concurrencyLevel(8)
            //设置写缓存后3秒钟过期
            .expireAfterWrite(3, TimeUnit.SECONDS)
            //设置缓存容器的初始容量为10
            .initialCapacity(10)
            //设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
            .maximumSize(1000)
            //设置要统计缓存的命中率
//            .recordStats()
            //build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
            .build();

    public static Object get(String key) throws Exception{
        return memCache.getIfPresent(key);
    }

    public static void put(String key, Object value){
        memCache.put(key,value);
    }

}

