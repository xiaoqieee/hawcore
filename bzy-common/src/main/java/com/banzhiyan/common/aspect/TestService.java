package com.banzhiyan.common.aspect;

import com.neo.xnol.pcts.common.annotation.ConfigSwitch;
import com.neo.xnol.pcts.common.annotation.DelayExecute;
import com.neo.xnol.pcts.common.annotation.InvokeLogger;
import com.neo.xnol.pcts.common.annotation.TaskProgress;
import com.neo.xnol.pcts.common.constant.TaskProgressEnum;
import com.neo.xnol.pcts.common.handler.TaskProgressHandler;
import com.neo.xnol.pcts.common.zk.ZKClientHandlerWrapper;
import ooh.bravo.core.dto.ResponseDTO;
import ooh.bravo.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Created by xn025665 on 2017/6/19.
 */
@Service
@InvokeLogger(logMessage = "测试方法")
public class TestService extends BaseService {

    @Autowired
    private ZKClientHandlerWrapper zkClientHandler;
    @Autowired
    private TaskProgressHandler taskProgressHandler;

//    @Autowired
//    private TransactionTemplate transactionTemplate;

    @DelayExecute
    @InvokeLogger
//    @RequestExecLimiter(concurrency = 10)
    public ResponseDTO<String> hello1() {
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (Exception e) {
        }
        ResponseDTO<String> result = new ResponseDTO<>();
        result.setData("Hello world");
        return result;
    }

    //    @IgnoreInvokeLogger
    @InvokeLogger
//    @DelayExecute(delay = 10)
    @ConfigSwitch(switchName = "test.switch")
    public String hello2(final String name) {
        return "hello2 " + name + "!";
    }

    @TaskProgress(task = TaskProgressEnum.TASK_BUSINESS_1)
    @InvokeLogger
    public void test11(){
        logger.info("开始进入方法");
        try{
            TimeUnit.MILLISECONDS.sleep((int)Math.random() * 20 + 10);
        }catch (Exception e){

        }
        logger.info("结束返回方法");
    }
}
