package com.banzhiyan.core.config;

import com.banzhiyan.core.util.PropertiesLoader;
import org.springframework.beans.factory.FactoryBean;

import java.util.List;

/**
 * Created by xn025665 on 2017/8/23.
 */
public class LocationsMetaSource implements FactoryBean<List<String>> {
    public LocationsMetaSource() {
    }

    public List<String> getObject() throws Exception {
        return PropertiesLoader.getLocations();
    }

    public Class<?> getObjectType() {
        return List.class;
    }

    public boolean isSingleton() {
        return true;
    }
}
