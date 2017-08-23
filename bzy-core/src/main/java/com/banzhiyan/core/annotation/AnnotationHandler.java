package com.banzhiyan.core.annotation;

import org.springframework.beans.factory.config.BeanDefinition;

import java.util.Map;
import java.util.Set;

/**
 * Created by xn025665 on 2017/8/23.
 */
public interface AnnotationHandler {
    void handle(Map<String, Set<BeanDefinition>> var1) throws Exception;
}
