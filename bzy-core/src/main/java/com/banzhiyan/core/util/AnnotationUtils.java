package com.banzhiyan.core.util;

import com.banzhiyan.core.annotation.ClasspathAnnotationDefinitionScanner;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class AnnotationUtils {
    public AnnotationUtils() {
    }

    public static Set<BeanDefinition> findAnnotationBeanDefinition(String[] basePackages, String annotationType) throws ClassNotFoundException {
        ClasspathAnnotationDefinitionScanner scanner = new ClasspathAnnotationDefinitionScanner();
        scanner.addIncludeFilter(new AnnotationTypeFilter((Class<? extends Annotation>)Class.forName(annotationType)));
        Set<BeanDefinition> candidates = new LinkedHashSet();
        String[] arr$ = basePackages;
        int len$ = basePackages.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String basePackage = arr$[i$];
            Iterator i = scanner.findCandidateComponents(basePackage).iterator();

            while(i.hasNext()) {
                BeanDefinition bd = (BeanDefinition)i.next();
                candidates.add(bd);
            }
        }

        return candidates;
    }
}
