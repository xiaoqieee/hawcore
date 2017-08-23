package com.banzhiyan.core.util;

import com.banzhiyan.core.config.ScannerConfigurer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class BeanUtils {
    public BeanUtils() {
    }

    public static String getAllBasePackagesString(ApplicationContext applicationContext) {
        StringBuilder packages = new StringBuilder(128);
        getBasePackages(applicationContext, packages);
        int length = packages.length();
        if (length > 0 && 44 == packages.charAt(length - 1)) {
            packages.setLength(length - 1);
        }

        return packages.toString();
    }

    private static void getBasePackages(ApplicationContext applicationContext, StringBuilder packages) {
        Map<String, ScannerConfigurer> basePackagesMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ScannerConfigurer.class, true, false);
        Iterator i$ = basePackagesMap.values().iterator();

        while (true) {
            String[] basePackages;
            do {
                do {
                    if (!i$.hasNext()) {
                        if (applicationContext.getParent() != null) {
                            getBasePackages(applicationContext.getParent(), packages);
                        }

                        return;
                    }

                    ScannerConfigurer configurer = (ScannerConfigurer) i$.next();
                    basePackages = configurer.getBasePackages();
                } while (basePackages == null);
            } while (basePackages.length <= 0);

            String[] arr$ = basePackages;
            int len = basePackages.length;

            for (int i = 0; i < len; ++i) {
                String basePackage = arr$[i];
                if (StringUtils.isNotBlank(basePackage)) {
                    packages.append(basePackage.trim());
                    packages.append(",");
                }
            }
        }
    }

    public static Set<String> getAllBasePackagesList(ApplicationContext applicationContext) {
        Map<String, Boolean> packagesMap = new HashMap();
        findBasePackages(applicationContext, packagesMap);
        return packagesMap.keySet();
    }

    private static void findBasePackages(ApplicationContext applicationContext, Map<String, Boolean> packagesMap) {
        Map<String, ScannerConfigurer> basePackagesMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ScannerConfigurer.class, true, false);
        String[] basePackages = null;
        Iterator i$ = basePackagesMap.values().iterator();

        while (true) {
            do {
                do {
                    if (!i$.hasNext()) {
                        if (applicationContext.getParent() != null) {
                            findBasePackages(applicationContext.getParent(), packagesMap);
                        }

                        return;
                    }

                    ScannerConfigurer configurer = (ScannerConfigurer) i$.next();
                    basePackages = configurer.getBasePackages();
                } while (basePackages == null);
            } while (basePackages.length <= 0);

            String[] arr$ = basePackages;
            int len = basePackages.length;

            for (int i = 0; i < len; ++i) {
                String basePackage = arr$[i];
                if (StringUtils.isNotBlank(basePackage)) {
                    packagesMap.put(basePackage.trim(), Boolean.valueOf(true));
                }
            }
        }
    }

    public static <T> Set<T> findAllBeans(ApplicationContext applicationContext, Class<T> type) {
        Map<T, Boolean> container = new HashMap();
        findBeans(applicationContext, container, type);
        return container.keySet();
    }

    private static <T> void findBeans(ApplicationContext applicationContext, Map<T, Boolean> container, Class<T> type) {
        Map<String, T> beansMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, type, true, false);
        Iterator i = beansMap.values().iterator();

        while (i.hasNext()) {
            T bean = (T) i.next();
            if (bean != null) {
                container.put(bean, Boolean.valueOf(true));
            }
        }

        if (applicationContext.getParent() != null) {
            findBeans(applicationContext.getParent(), container, type);
        }

    }
}

