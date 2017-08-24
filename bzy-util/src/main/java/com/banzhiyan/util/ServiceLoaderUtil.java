package com.banzhiyan.util;

import com.google.common.collect.Lists;

import java.util.*;

/**
 * Created by xn025665 on 2017/8/24.
 */

public abstract class ServiceLoaderUtil {
    public ServiceLoaderUtil() {
    }

    public static <T> List<T> loadServicesList(Class<T> serviceInterfaceType) throws IllegalArgumentException {
        return Collections.unmodifiableList(loadServicesList0(serviceInterfaceType));
    }

    private static <T> List<T> loadServicesList0(Class<T> serviceInterfaceType) throws IllegalArgumentException {
        ClassLoader classLoader = ClassLoaderUtil.currentClassLoader();
        ServiceLoader<T> serviceLoader = ServiceLoader.load(serviceInterfaceType, classLoader);
        Iterator<T> iterator = serviceLoader.iterator();
        LinkedList serviceList = Lists.newLinkedList();

        while(iterator.hasNext()) {
            serviceList.add(iterator.next());
        }

        if(serviceList.isEmpty()) {
            String className = serviceInterfaceType.getName();
            String message = String.format("没有定义任何%s的实现类在配置文件/META-INF/services/%s中", new Object[]{className, className});
            throw new IllegalArgumentException(message);
        } else {
            return serviceList;
        }
    }

    public static <T> T loadFirstService(Class<T> serviceInterfaceType) throws IllegalArgumentException {
        List<T> serviceList = loadServicesList0(serviceInterfaceType);
        return serviceList.get(0);
    }

    public static <T> T loadLastService(Class<T> serviceInterfaceType) throws IllegalArgumentException {
        List<T> serviceList = loadServicesList0(serviceInterfaceType);
        return serviceList.get(serviceList.size() - 1);
    }
}

