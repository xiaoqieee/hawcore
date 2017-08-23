package com.banzhiyan.common.aspect;

import com.banzhiyan.common.annotation.DelayExecute;
import com.banzhiyan.core.support.ParameterNameHelper;
import com.banzhiyan.logging.Logger;
import com.banzhiyan.logging.LoggerFactory;
import com.banzhiyan.thread.DelayThreadManager;
import com.neo.xnol.pcts.common.annotation.DelayExecute;
import com.neo.xnol.pcts.common.thread.DelayThreadManager;
import ooh.bravo.core.support.ParameterNameHelper;
import ooh.bravo.logging.Logger;
import ooh.bravo.logging.LoggerFactory;
import ooh.bravo.util.JsonUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * Created by xn025665 on 2017/6/17.
 */
@Aspect
@Component
public class DelayExecuteAspect implements Ordered {

    Logger logger = LoggerFactory.getLogger(DelayExecuteAspect.class);

    @Pointcut("@annotation(com.neo.xnol.pcts.common.annotation.DelayExecute)")
    public void delay() {
    }

    @Around(value = "delay()")
    public void around(final ProceedingJoinPoint joinPoint) throws Throwable {
        final Method currentMethod = this.currentMethod(joinPoint);
        DelayExecute delayExecute = currentMethod.getAnnotation(DelayExecute.class);

        final int delay = delayExecute.delay();
        final TimeUnit timeUnit = delayExecute.timeUnit();
        DelayThreadManager.addTask(new Runnable() {
            @Override
            public void run() {
                try {
                    joinPoint.proceed();
                } catch (Throwable t) {
                    logger.error("延迟执行任务异常，参数：{}，异常信息:{}", getParameter(currentMethod, joinPoint.getArgs()),t);
                }
            }
        }, delay, timeUnit);
    }

    private String getParameter(Method method, Object[] args) {
        StringBuffer sb = new StringBuffer();
        String[] parameterNames = ParameterNameHelper.getDiscoverer().getParameterNames(method);
        if (null != parameterNames && parameterNames.length > 0) {
            int i = 0;
            for (int length = parameterNames.length; i < length; ++i) {
                sb.append(parameterNames[i]).append(":");
                sb.append(JsonUtils.toJSON(args[i]));
                if (i < length - 1) {
                    sb.append(", ");
                }
            }
        }
        return sb.toString();
    }

    ;

    private Method currentMethod(JoinPoint jp) {
        return ((MethodSignature) jp.getSignature()).getMethod();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
