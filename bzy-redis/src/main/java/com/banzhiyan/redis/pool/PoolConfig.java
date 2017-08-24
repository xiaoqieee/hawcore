package com.banzhiyan.redis.pool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Created by xn025665 on 2017/8/24.
 */
public class PoolConfig extends GenericObjectPoolConfig {
    public PoolConfig() {
        this.setJmxEnabled(false);
        this.setTestOnBorrow(true);
        this.setTestWhileIdle(true);
        this.setMaxWaitMillis(1500L);
        this.setNumTestsPerEvictionRun(16);
        this.setMinEvictableIdleTimeMillis(60000L);
        this.setTimeBetweenEvictionRunsMillis(30000L);
    }
}
