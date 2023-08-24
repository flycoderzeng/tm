package com.tm.worker.core.control;

import com.tm.worker.core.exception.TMException;
import com.tm.worker.core.threads.AutoTestContext;
import com.tm.worker.core.threads.AutoTestContextService;
import com.tm.worker.core.variable.AutoTestVariables;
import com.tm.worker.utils.ExpressionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class LoopController extends GenericController {
    private Integer total = -1;
    @Override
    public void run() {
        log.info("执行LOOP逻辑，{}", getName());
        AutoTestContext context = AutoTestContextService.getContext();
        AutoTestVariables caseVariables = context.getCaseVariables();
        if(StringUtils.isBlank(condition)) {
            throw new TMException("固定循环次数值不能为空");
        }

        if(total <= -1) {
            String result = ExpressionUtils.replaceExpression(condition, caseVariables.getVariables());
            if(StringUtils.isNotBlank(result)) {
                Double count = Double.valueOf(result);
                total = count.intValue();
                if(total > 10000) {
                    total = 10000;
                }
                count();
            }else{
                throw new TMException("循环次数值表达氏的结果必须是整数，计算结果是：" + result);
            }
        }else{
           count();
        }
    }

    private void count() {
        log.info("loop 计算后的值是：{}", total);
        addResultInfo("loop 计算后的值是：").addResultInfoLine(total);
        if (total <= 0) {
            setBreakLoop(true);
        } else {
            total--;
        }
    }
}
