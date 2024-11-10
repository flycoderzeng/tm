package com.tm.worker.core.node.function.time;

import com.tm.worker.core.exception.TMException;
import com.tm.worker.core.node.function.FunctionNode;
import com.tm.worker.core.threads.AutoTestContext;
import com.tm.worker.core.threads.AutoTestContextService;
import com.tm.worker.core.variable.AutoTestVariables;
import com.tm.worker.utils.ExpressionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;

@Slf4j
public class SleepNode extends FunctionNode {
    private static final String ARG_1 = "seconds";

    @Override
    public void run() throws Exception {
        super.run();
        log.info("执行平台api：休眠");
        AutoTestContext context = AutoTestContextService.getContext();
        AutoTestVariables caseVariables = context.getCaseVariables();
        String secondsStr;
        if (!parametersMap.containsKey(ARG_1)) {
            secondsStr = "0";
        }else {
            secondsStr = parametersMap.get(ARG_1);
            secondsStr = ExpressionUtils.replaceExpression(secondsStr, caseVariables.getVariables());
            if (StringUtils.isBlank(secondsStr)) {
                secondsStr = "0";
            }else{
                secondsStr = ExpressionUtils.executeExpression(secondsStr, caseVariables.getVariables());
            }
        }
        if(!StringUtils.isNumeric(secondsStr)) {
            throw new TMException("seconds 必须是整数");
        }
        addResultInfo("休眠秒数: ").addResultInfoLine(secondsStr);
        int seconds = Integer.parseInt(secondsStr);
        if(seconds > 24 * 3600) {
            throw new TMException("seconds 必须小于24 * 3600 秒");
        }
        if(seconds > 0) {
            TimeUnit.SECONDS.sleep(seconds);
        }
    }
}
