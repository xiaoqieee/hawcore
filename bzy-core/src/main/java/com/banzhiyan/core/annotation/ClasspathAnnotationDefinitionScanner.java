package com.banzhiyan.core.annotation;

import com.banzhiyan.core.util.AnnotationUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xn025665 on 2017/8/23.
 */
public class ClasspathAnnotationDefinitionScanner extends ClassPathScanningCandidateComponentProvider {
    private Map<String, Set<BeanDefinition>> annotationConfigs;

    public ClasspathAnnotationDefinitionScanner() {
        super(false);
    }

    public Map<String, Set<BeanDefinition>> getAnnotationConfigs() {
        return this.annotationConfigs;
    }

    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        return (metadata.isInterface() || metadata.isAbstract() || metadata.isConcrete() || metadata.isFinal()) && metadata.isIndependent();
    }

    public void findAllAnnotationConfig(String[] allAnnotationTypes, String[] basePackages) throws ClassNotFoundException {
        Map<String, Set<BeanDefinition>> annonConfigs = new ConcurrentHashMap();
        String[] arr$ = allAnnotationTypes;
        int len$ = allAnnotationTypes.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String annotationType = arr$[i$];
            Set<BeanDefinition> candidates = AnnotationUtils.findAnnotationBeanDefinition(basePackages, annotationType);
            annonConfigs.put(annotationType, candidates);
        }

        this.annotationConfigs = Collections.unmodifiableMap(annonConfigs);
    }
}
