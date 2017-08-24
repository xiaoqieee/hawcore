package com.banzhiyan.redis.sentinel;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;

import java.util.Set;

/**
 * Created by xn025665 on 2017/8/24.
 */

public class RedisSentinelConfigurationFactory implements FactoryBean<RedisSentinelConfiguration> {
    private final String masterName;
    private final Set<String> sentinels;

    public RedisSentinelConfigurationFactory(String masterName, Set<String> sentinels) {
        this.masterName = masterName;
        this.sentinels = sentinels;
    }

    public RedisSentinelConfiguration getObject() throws Exception {
        return new RedisSentinelConfiguration(this.masterName, this.sentinels);
    }

    public Class<?> getObjectType() {
        return RedisSentinelConfiguration.class;
    }

    public boolean isSingleton() {
        return true;
    }
}