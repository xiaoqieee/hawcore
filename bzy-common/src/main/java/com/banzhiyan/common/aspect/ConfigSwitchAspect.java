package com.banzhiyan.common.aspect;

import com.banzhiyan.common.annotation.ConfigSwitch;
import com.banzhiyan.logging.Logger;
import com.banzhiyan.logging.LoggerFactory;
import com.banzhiyan.util.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by xn025665 on 2017/6/17.
 */
@Aspect
@Component
public class ConfigSwitchAspect implements Ordered {

    private final transient Logger logger = LoggerFactory.getLogger(ConfigSwitchAspect.class);

    @Pointcut("@annotation(com.neo.xnol.pcts.common.annotation.ConfigSwitch)")
    public void filter() {
    }

    @Around(value = "filter()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method currentMethod = this.currentMethod(joinPoint);
        ConfigSwitch configSwitch = currentMethod.getAnnotation(ConfigSwitch.class);
        String name = configSwitch.switchName();
        boolean isOn = false;
        if (StringUtils.isNotBlank(name)) {
//            String switchs = BaseBusinessParamService.getParam(name);
            String switchs = "";
            if ("true".equals(switchs) || "1".equals(switchs) || "on".equals(switchs)) {
                isOn = true;
            }
        }
        if (isOn) {
            return joinPoint.proceed();
        } else {
            logger.warn("Switch config named '"+ name+ "' is off, return null directly! If need to really execute the method, set the switch value to 1|on|true on config center.");
            return null;
        }
    }

    private Method currentMethod(JoinPoint jp) {
        return ((MethodSignature) jp.getSignature()).getMethod();
    }

    @Override
    public int getOrder() {
        return 100;
    }
}
