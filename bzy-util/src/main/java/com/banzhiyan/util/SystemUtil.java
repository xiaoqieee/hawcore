package com.banzhiyan.util;

import org.apache.commons.lang3.SystemUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * Created by xn025665 on 2017/8/24.
 */
public class SystemUtil {
    private static final boolean isHotspotVM;
    private static final RuntimeMXBean runtimeMXBean;

    public SystemUtil() {
    }

    public static String getBootClassPath() {
        return runtimeMXBean.isBootClassPathSupported()?null:runtimeMXBean.getBootClassPath();
    }

    public static String getClassPath() {
        return runtimeMXBean.getClassPath();
    }

    public static boolean isHotSpotVM() {
        return isHotspotVM;
    }

    static {
        isHotspotVM = SystemUtils.JAVA_VM_NAME.contains("HotSpot");
        runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    }
}
