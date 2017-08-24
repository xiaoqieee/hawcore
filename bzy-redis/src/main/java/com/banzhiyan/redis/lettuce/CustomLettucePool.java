package com.banzhiyan.redis.lettuce;

import com.banzhiyan.redis.pool.PoolConfig;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.DefaultLettucePool;

/**
 * Created by xn025665 on 2017/8/24.
 */
class CustomLettucePool extends DefaultLettucePool {
    public CustomLettucePool(RedisSentinelConfiguration sentinelConfiguration) {
        super(sentinelConfiguration);
        super.setPoolConfig(this.buildPoolConfig());
    }

    private PoolConfig buildPoolConfig() {
        PoolConfig config = new PoolConfig();
        config.setMaxTotal(512);
        config.setMaxIdle(32);
        return config;
    }
}
