package com.banzhiyan.redis.lettuce;

import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePool;

/**
 * Created by xn025665 on 2017/8/24.
 */

class CustomLettuceConnectionFactory extends LettuceConnectionFactory {
    public CustomLettuceConnectionFactory(LettucePool lettucePool, int database) {
        super(lettucePool);
        super.setDatabase(database);
        super.setShareNativeConnection(true);
    }

    public void afterPropertiesSet() {
        super.afterPropertiesSet();
    }

    public void destroy() {
        super.destroy();
    }
}

