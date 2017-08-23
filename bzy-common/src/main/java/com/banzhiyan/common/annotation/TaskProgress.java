package com.banzhiyan.common.annotation;

import com.neo.xnol.pcts.common.constant.TaskProgressEnum;
import org.springframework.beans.factory.annotation.Required;

import java.lang.annotation.*;

/**
 * 将此注解让在一个任务的同步最小单元处理方法上，可以记录任务的开始和结束
 *
 * Created by xn025665 on 2017/8/10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface TaskProgress {

    @Required
    TaskProgressEnum task();
}
