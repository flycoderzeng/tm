package com.tm.worker.core.control;

import com.tm.worker.core.exception.TMException;
import com.tm.worker.core.node.StepNodeBase;
import com.tm.worker.core.threads.AutoTestContext;
import com.tm.worker.core.threads.AutoTestContextService;
import com.tm.worker.core.variable.AutoTestVariables;
import com.tm.worker.utils.ExpressionUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@Data
public class GenericController extends StepNodeBase {
    protected String condition;

    protected boolean breakLoop = false;

    @Override
    public void run() {
        super.run();
        AutoTestContext context = AutoTestContextService.getContext();
        AutoTestVariables caseVariables = context.getCaseVariables();
        if(StringUtils.isBlank(condition)) {
            throw new TMException("条件表达式不能为空");
        }
        String result = ExpressionUtils.replaceExpression(condition, caseVariables.getVariables());
        log.info(result);
        addResultInfo("表达式：").addResultInfoLine(result);
        if(StringUtils.isBlank(result)) {
            throw new TMException("条件表达式不能为空");
        }
        result = ExpressionUtils.executeExpression(result, caseVariables.getVariables());
        log.info(result);
        addResultInfo("表达式结果：").addResultInfoLine(result);
        if("true".contentEquals(result) || "false".contentEquals(result)) {
            if("false".contentEquals(result)) {
                setBreakLoop(true);
            }
        }else{
            throw new TMException("条件表达氏的结果必须是true或者false，计算结果是：" + result);
        }
    }
}
