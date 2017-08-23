package com.banzhiyan.common.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 延迟执行(此注解只能用于不需要返回值的方法)
 *
 * Created by xn025665 on 2017/7/25
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface DelayExecute {

    // 延迟时间
    int delay() default 30;

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
