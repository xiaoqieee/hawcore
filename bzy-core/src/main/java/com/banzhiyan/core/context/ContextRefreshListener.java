package com.banzhiyan.core.context;

import com.banzhiyan.core.annotation.AnnotationHandler;
import com.banzhiyan.core.annotation.ClasspathAnnotationDefinitionScanner;
import com.banzhiyan.core.config.ScannerConfigurer;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class ContextRefreshListener implements ApplicationListener<ContextRefreshedEvent> {
    private final ClasspathAnnotationDefinitionScanner scanner;

    public ContextRefreshListener(ClasspathAnnotationDefinitionScanner scanner) {
        this.scanner = scanner;
    }

    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();

        try {
            this.scan(applicationContext);
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }

    private void scan(ApplicationContext applicationContext) throws Exception {
        Map<String, ScannerConfigurer> basePackagesMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ScannerConfigurer.class, true, false);
        String[] allBasePackages = null;
        String[] allAnnotationTypes = null;
        Iterator iterator = basePackagesMap.values().iterator();

        while(iterator.hasNext()) {
            ScannerConfigurer scannerConfigurer = (ScannerConfigurer)iterator.next();
            String[] basePackages = scannerConfigurer.getBasePackages();
            if(basePackages != null) {
                allBasePackages = (String[]) ArrayUtils.addAll(allBasePackages, basePackages);
            }

            String[] annotationTypes = scannerConfigurer.getAnnotationTypes();
            if(annotationTypes != null) {
                allAnnotationTypes = (String[])ArrayUtils.addAll(allAnnotationTypes, annotationTypes);
            }
        }

        if(allBasePackages != null && allAnnotationTypes != null) {
            try {
                this.scanner.findAllAnnotationConfig(allAnnotationTypes, allBasePackages);
            } catch (Exception var9) {
                throw new RuntimeException(var9);
            }

            Map<String, AnnotationHandler> annotationHandlersMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, AnnotationHandler.class, true, false);
            iterator = annotationHandlersMap.values().iterator();

            while(iterator.hasNext()) {
                AnnotationHandler handler = (AnnotationHandler)iterator.next();
                if(handler != null) {
                    handler.handle(this.scanner.getAnnotationConfigs());
                }
            }
        }

    }
}

