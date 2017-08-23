package com.banzhiyan.core.support;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

/**
 * Created by xn025665 on 2017/8/23.
 */
public final class ParameterNameHelper {
    private static final DefaultParameterNameDiscoverer DISCOVERER = new DefaultParameterNameDiscoverer();

    public ParameterNameHelper() {
    }

    public static ParameterNameDiscoverer getDiscoverer() {
        return DISCOVERER;
    }
}
