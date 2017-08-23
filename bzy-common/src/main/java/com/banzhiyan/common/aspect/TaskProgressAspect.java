package com.banzhiyan.common.aspect;

import com.neo.xnol.pcts.common.annotation.TaskProgress;
import com.neo.xnol.pcts.common.constant.TaskProgressEnum;
import com.neo.xnol.pcts.common.handler.TaskProgressHandler;
import com.neo.xnol.pcts.common.handler.task.AbstractTaskProgressHandler;
import com.neo.xnol.pcts.common.utils.TaskProgressUtils;
import com.neo.xnol.pcts.common.zk.ZKClientHandlerWrapper;
import com.neo.xnol.pcts.utils.SpringContext;
import ooh.bravo.logging.Logger;
import ooh.bravo.logging.LoggerFactory;
import ooh.bravo.util.ObjectId;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by xn025665 on 2017/8/10.
 */
@Aspect
@Component
public class TaskProgressAspect implements Ordered {


    @Autowired
    private ZKClientHandlerWrapper zkClientHandler;
    @Autowired
    private TaskProgressHandler taskProgressHandler;

    Logger logger = LoggerFactory.getLogger(TaskProgressAspect.class);

    ThreadLocal<String> threadLocal = new ThreadLocal<>();

    @Pointcut("@annotation(com.neo.xnol.pcts.common.annotation.TaskProgress)")
    public void task() {
    }

    @Before(value = "task()")
    public void before(JoinPoint joinPoint) throws Throwable {
        handleBegin(joinPoint);
    }

    @AfterReturning(pointcut = "task()", returning = "returnResult")
    public void afterReturn(JoinPoint joinPoint, Object returnResult) {
        handleEnd(joinPoint);
    }

    @AfterThrowing(pointcut = "task()", throwing = "t")
    public void e(JoinPoint joinPoint, Throwable t) {
        handleEnd(joinPoint);
    }

    private void handleBegin(JoinPoint joinPoint) {
        TaskProgressEnum task = getTaskEnum(joinPoint);
        if (taskProgressHandler.addTaskIfAbsent(task)) {
            AbstractTaskProgressHandler taskBeginHandler = SpringContext.getBean(task.getTaskProgressHandlerClass());
            taskBeginHandler.handTaskBegin(task);
        }
        taskProgressHandler.addLocalTaskIfAbsent(task);

        String unitId = ObjectId.getIdentityId();
        threadLocal.set(unitId);
        String unitPath = TaskProgressUtils.getUnitTaskNode(task, unitId);
        zkClientHandler.createPersistentNode(unitPath, "1");
    }

    private void handleEnd(JoinPoint joinPoint) {
        TaskProgressEnum task = getTaskEnum(joinPoint);
        String unitId = threadLocal.get();
        String unitPath = TaskProgressUtils.getUnitTaskNode(task, unitId);
        zkClientHandler.deleteNode(unitPath);
    }

    private TaskProgressEnum getTaskEnum(JoinPoint joinPoint) {
        final Method currentMethod = this.currentMethod(joinPoint);
        TaskProgress taskProgress = currentMethod.getAnnotation(TaskProgress.class);
        TaskProgressEnum task = taskProgress.task();
        return task;
    }


    private Method currentMethod(JoinPoint jp) {
        return ((MethodSignature) jp.getSignature()).getMethod();
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
