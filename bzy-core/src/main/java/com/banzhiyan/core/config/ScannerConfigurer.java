package com.banzhiyan.core.config;

/**
 * Created by xn025665 on 2017/8/23.
 */
public class ScannerConfigurer {
    private String[] basePackages;
    private String[] annotationTypes;

    public ScannerConfigurer() {
    }

    public String[] getBasePackages() {
        return this.basePackages;
    }

    public void setBasePackages(String[] basePackages) {
        this.basePackages = basePackages;
    }

    public String[] getAnnotationTypes() {
        return this.annotationTypes;
    }

    public void setAnnotationTypes(String[] annotationTypes) {
        this.annotationTypes = annotationTypes;
    }
}
