package com.banzhiyan.log.annotation;

import java.lang.annotation.*;

/**
 * Created by xn025665 on 2017/8/23.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Log {
    String value() default "";

    Level level() default Level.INFO;
}
