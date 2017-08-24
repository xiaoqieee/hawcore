package com.banzhiyan.tolerance.annotation;

import java.lang.annotation.*;

/**
 * Created by xn025665 on 2017/8/24.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestThrottle {
    String subscription() default "";

    int concurrency() default 15;

    int timeout() default 0;

    boolean failfast() default false;

    boolean failsafe() default true;
}

