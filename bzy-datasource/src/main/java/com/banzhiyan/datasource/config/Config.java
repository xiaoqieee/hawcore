package com.banzhiyan.datasource.config;

import com.banzhiyan.util.PropertiesUtils;

import java.util.Properties;

/**
 * Created by xn025665 on 2017/8/24.
 */
abstract class Config {
    private static final Properties PROPS = PropertiesUtils.loadProperties("classpath:DataSourceConfig.properties");

    Config() {
    }

    static final String get(String key) {
        return PROPS.getProperty(key);
    }

    static final int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    static final long getLong(String key) {
        return Long.parseLong(get(key));
    }

    static final boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }
}
