package com.banzhiyan.log.annotation;

import java.lang.annotation.*;

/**
 * Created by xn025665 on 2017/8/23.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreLog {
}
