package com.tm.worker.core.node.function.check;

import com.tm.worker.core.exception.AssertionException;
import com.tm.worker.core.exception.ConditionExpressionResultException;
import com.tm.worker.core.node.function.FunctionNode;
import com.tm.worker.core.threads.AutoTestContext;
import com.tm.worker.core.threads.AutoTestContextService;
import com.tm.worker.core.variable.AutoTestVariables;
import com.tm.worker.utils.ExpressionUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


@Slf4j
public class AssertNode extends FunctionNode {
    private static final String ARG_1 = "expression";

    @SneakyThrows
    @Override
    public void run() {
        super.run();
        log.info("执行平台api：断言");
        String expression = getArgStringValue(ARG_1);
        log.info("断言的字符串: {}", expression);
        addResultInfo("断言的字符串: ").addResultInfoLine(expression);
        if(StringUtils.isBlank(expression)) {
            addResultInfoLine("断言的字符串是空");
            return ;
        }

        AutoTestContext context = AutoTestContextService.getContext();
        AutoTestVariables caseVariables = context.getCaseVariables();
        final String expr = ExpressionUtils.replaceExpression(expression, caseVariables.getVariables());
        log.info("断言式: {}", expr);
        addResultInfo("断言式: ").addResultInfoLine(expr);
        String result = ExpressionUtils.executeExpression(expr, caseVariables.getVariables());
        log.info("断言的结果是：{}", result);
        addResultInfo("result: ").addResultInfoLine(result);
        if("true".contentEquals(result) || "false".contentEquals(result)) {
            if("false".contentEquals(result)) {
                throw new AssertionException("断言不通过");
            }
        }else{
            throw new ConditionExpressionResultException("表达氏的结果必须是true或者false，计算结果是：" + result);
        }
    }
}
