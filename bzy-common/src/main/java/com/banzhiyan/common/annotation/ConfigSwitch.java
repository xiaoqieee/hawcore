package com.banzhiyan.common.annotation;

import org.springframework.beans.factory.annotation.Required;

import java.lang.annotation.*;

/**
 * 配置开关（支持用配置中心来做相应的开关）
 *
 * Created by xn025665 on 2017/8/10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ConfigSwitch {

    @Required
    String switchName();
}
