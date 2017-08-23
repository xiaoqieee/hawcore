package com.banzhiyan.common.annotation;

import java.lang.annotation.*;

/**
 * 忽略InvokeLogger注解处理，用于在某个类上添加了InvokeLogger注解但需要忽略某个方法的InvokeLogger注解处理时。
 *
 * Created by xn025665 on 2017/6/17.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface IgnoreInvokeLogger {

}
