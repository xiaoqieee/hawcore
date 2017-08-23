package com.banzhiyan.common.annotation;

import java.lang.annotation.*;

/**
 * 使用此注解时，目标类必须继承ooh.bravo.service.BaseService类。
 * 可以添加到具体方法上，也可以添加到类上(等同于添加到了类中所有的方法上)，如果方法和类都添加了此注解时，以方法上的注解为准
 *
 * Created by xn025665 on 2017/6/17.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface InvokeLogger {
    // 打印类型 原始
    public static int LOG_VALUE_TYPE_ORIGINAL = 1;
    // 打印类型 json
    public static int LOG_VALUE_TYPE_JSON = 2;

    // 打印日志消息
    String logMessage() default "";
    // 需要打印的参数(可多个，用:号隔开，默认打印全部参数)
    String logParamName() default "";
    // 是否打印入口
    boolean logEntrance() default true;
    // 是否打印出口
    boolean logReturn() default true;
    // 值打印类型（默认为json格式）
    int valueType() default LOG_VALUE_TYPE_JSON;
}
