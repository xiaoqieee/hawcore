package com.banzhiyan.common.aspect;

import com.neo.config.annotion.ConfigFieldAnnotion;
import com.neo.xnol.pcts.common.annotation.IgnoreInvokeLogger;
import com.neo.xnol.pcts.common.annotation.InvokeLogger;
import com.neo.xnol.pcts.product.constant.ProductResult;
import com.neo.xnol.pcts.product.exception.ProductException;
import ooh.bravo.core.support.ParameterNameHelper;
import ooh.bravo.logging.Logger;
import ooh.bravo.util.JsonUtils;
import ooh.bravo.util.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by xn025665 on 2017/6/17.
 */
@Aspect
@Component
public class InvokeLoggerAspect implements Ordered {

    private static String FIELD_LOGGER_NAME = "logger";

    @ConfigFieldAnnotion(path = "invoke.logger.print.result", defaultValue = "false")
    private String printResult = "false";


    @Pointcut("@within(com.neo.xnol.pcts.common.annotation.InvokeLogger)")
    public void logType() {
    }

    @Pointcut("@annotation(com.neo.xnol.pcts.common.annotation.InvokeLogger)")
    public void logMethod() {
    }

    @Around(value="logType() || logMethod()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method currentMethod = this.currentMethod(joinPoint);
        InvokeLogger invokeLogger = currentMethod.getAnnotation(InvokeLogger.class);
        if(null == invokeLogger){
            Class<?> currentClass = this.currentClass(joinPoint);
            invokeLogger = (InvokeLogger)currentClass.getAnnotation(InvokeLogger.class);
        }
        if(null != invokeLogger){
            IgnoreInvokeLogger ignoreInvokeLogger = currentMethod.getAnnotation(IgnoreInvokeLogger.class);
            if(null != ignoreInvokeLogger){
                return joinPoint.proceed();
            }else{
                Logger logger = getLogger(joinPoint);
                if(invokeLogger.logEntrance()){
                    logger.info(buildEntranceLog(joinPoint, currentMethod, invokeLogger));
                }
                long start = System.currentTimeMillis();
                Object result = joinPoint.proceed();
                long end = System.currentTimeMillis();
                if(invokeLogger.logReturn()){
                    if(logger.isDebugEnabled() || printResult()){
                        logger.info(buildReturnLog(currentMethod, invokeLogger, result, end-start));
                    }else{
                        logger.info(buildReturnLog(currentMethod, invokeLogger, end-start));
                    }
                }
                return result;
            }
        }
        throw new ProductException(ProductResult.PRODUCT_CODE_EXCEPTION,"Can not find @InvokeLogger annotation!");
    }

//    @Around("logMethod()")
//    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
//        Method currentMethod = this.currentMethod(joinPoint);
//        InvokeLogger invokeLogger = currentMethod.getAnnotation(InvokeLogger.class);
//        Logger logger = getLogger(joinPoint);
//        if(invokeLogger.logEntrance()){
//            logger.info(buildEntranceLog(joinPoint, currentMethod, invokeLogger));
//        }
//        Object result = joinPoint.proceed();
//        if(invokeLogger.logReturn()){
//            logger.info(buildReturnLog(currentMethod, invokeLogger, result));
//        }
//        return result;
//    }

    private String buildEntranceLog(ProceedingJoinPoint joinPoint, Method method, InvokeLogger invokeLogger){
        StringBuffer sb = new StringBuffer();
        sb.append(getMethodInfo(method));
        if(StringUtils.isNotBlank(invokeLogger.logMessage())){
            sb.append(invokeLogger.logMessage()).append(" - ");
        }
        sb.append("Entrance. ").append(getLogParameter(method, invokeLogger, joinPoint.getArgs()));
        return sb.toString();
    }

    private String buildReturnLog( Method method, InvokeLogger invokeLogger, Object result, long costTime){
        StringBuffer sb = new StringBuffer();
        sb.append(getMethodInfo(method));
        if(StringUtils.isNotBlank(invokeLogger.logMessage())){
            sb.append(invokeLogger.logMessage()).append(" - ");
        }
        sb.append("Return. ").append(getLogResult(invokeLogger, result)).append(" - ["+ costTime +"ms]");
        return sb.toString();
    }

    private String buildReturnLog( Method method, InvokeLogger invokeLogger, long costTime){
        StringBuffer sb = new StringBuffer();
        sb.append(getMethodInfo(method));
        if(StringUtils.isNotBlank(invokeLogger.logMessage())){
            sb.append(invokeLogger.logMessage()).append(" - ");
        }
        sb.append("Return. ").append(" ["+ costTime +"ms]");
        return sb.toString();
    }

    private Logger getLogger(ProceedingJoinPoint joinPoint) throws Throwable{
        Class<?> rootClazz = joinPoint.getTarget().getClass();
        Logger logger = getLogger(joinPoint,rootClazz, rootClazz);
        return logger;
    }

    private Logger getLogger(ProceedingJoinPoint joinPoint, Class<?> clazz, Class<?> rootClazz) throws Exception{
        if(clazz.getName().equals("java.lang.Object")){
            throw new ProductException(ProductResult.PRODUCT_CODE_EXCEPTION,"Annotation @InvokeLogger's target class("+rootClazz.getName()+") must extends ooh.bravo.service.BaseService");
        }
        if(hasLoggerField(clazz)){
            Field f = clazz.getDeclaredField("logger");
            f.setAccessible(true);
            return (Logger)f.get(joinPoint.getTarget());
        }else{
            return getLogger(joinPoint, clazz.getSuperclass(), rootClazz);
        }
    }

    public static void main(String[] args) {
        Object o = new Object();
        System.out.println(o.getClass().getName());
    }

    private boolean hasLoggerField(Class<?> clazz){
        Field[] fields = clazz.getDeclaredFields();
        if(null != fields){
            for(int i=0,len = fields.length;i<len;++i){
                if(FIELD_LOGGER_NAME.equals(fields[i].getName())){
                    return true;
                }
            }
        }
        return false;
    }

    private Method currentMethod(JoinPoint jp) {
        return ((MethodSignature)jp.getSignature()).getMethod();
    }

    private Class<?> currentClass(JoinPoint jp) {
        return jp.getTarget().getClass();
    }

    private String getMethodInfo(Method method){
        StringBuffer sb = new StringBuffer();
        sb.append(method.getName()).append(" - ");
        return sb.toString();
    }

    private String getLogResult(InvokeLogger invokeLogger, Object result){
        StringBuffer sb = new StringBuffer();
        int valueType = invokeLogger.valueType();
        sb.append("Return result is: ");
        if(InvokeLogger.LOG_VALUE_TYPE_ORIGINAL == valueType){
            sb.append(result);
        }else if(InvokeLogger.LOG_VALUE_TYPE_JSON == valueType){
            sb.append(JsonUtils.toJSON(result));
        }
        return sb.toString();
    }

    private String getLogParameter(Method method, InvokeLogger invokeLogger, Object[] args){
        StringBuffer sb = new StringBuffer();
        String logParamName = invokeLogger.logParamName();
        int valueType = invokeLogger.valueType();
        String[] parameterNames = ParameterNameHelper.getDiscoverer().getParameterNames(method);

        if(StringUtils.isBlank(logParamName)){
            if(null != parameterNames && parameterNames.length > 0){
                int i = 0;
                sb.append("Parameters is: ");
                for(int length = parameterNames.length; i < length; ++i) {
                    sb.append(parameterNames[i]).append(":");
                    if(InvokeLogger.LOG_VALUE_TYPE_ORIGINAL == valueType){
                        sb.append(args[i]);
                    }else if(InvokeLogger.LOG_VALUE_TYPE_JSON == valueType){
                        sb.append(JsonUtils.toJSON(args[i]));
                    }
                    if(i < length-1){
                        sb.append(", ");
                    }
                }
            }
        }else{
            String[] logParamNames = logParamName.split(":");
            if(null != logParamNames && logParamNames.length > 0){
                for(int i=0,length=logParamNames.length; i<length; ++i){
                    int idx = getArgIndex(parameterNames,logParamNames[i]);
                    if(idx > -1){
                        sb.append(logParamNames[i]).append(":");
                        if(InvokeLogger.LOG_VALUE_TYPE_ORIGINAL == valueType){
                            sb.append(args[idx]);
                        }else if(InvokeLogger.LOG_VALUE_TYPE_JSON == valueType){
                            sb.append(JsonUtils.toJSON(args[idx]));
                        }
                        if(i < length-1){
                            sb.append(", ");
                        }
                    }
                }
            }
        }
        return sb.toString();
    };

    private int getArgIndex(String[] logParamNames, String argName){
        if(null != logParamNames && logParamNames.length > 0){
            for(int i=0,length=logParamNames.length;i<length;++i){
                if(argName.equalsIgnoreCase(logParamNames[i])){
                    return i;
                }
            }
        }
        return -1;
    };

    private boolean printResult(){
        if(null != printResult){
            return "true".equalsIgnoreCase(printResult) || "1".equals(printResult);
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 50;
    }
}
