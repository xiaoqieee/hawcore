package com.banzhiyan.core.util;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class ApplicationContextHolder implements ApplicationContextAware, DisposableBean {
    private static ApplicationContext context;

    public ApplicationContextHolder() {
    }

    public static ApplicationContext get() {
        return context;
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        return context.getBean(name, requiredType);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        setContext(applicationContext);
    }

    private static void setContext(ApplicationContext context) {
        context = context;
    }

    public void destroy() {
        setContext((ApplicationContext)null);
    }
}

