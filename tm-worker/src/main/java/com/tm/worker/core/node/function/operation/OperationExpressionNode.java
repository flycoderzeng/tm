package com.tm.worker.core.node.function.operation;

import com.tm.worker.core.node.function.FunctionNode;
import com.tm.worker.core.threads.AutoTestContext;
import com.tm.worker.core.threads.AutoTestContextService;
import com.tm.worker.core.variable.AutoTestVariables;
import com.tm.worker.utils.ExpressionUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OperationExpressionNode extends FunctionNode {
    private static final String ARG_0 = "expression_0";
    private static final String ARG_1 = "expression_1";
    private static final String ARG_2 = "expression_2";
    private static final String ARG_3 = "expression_3";
    private static final String ARG_4 = "expression_4";

    private static final String OUT_0 = "result_0";
    private static final String OUT_1 = "result_1";
    private static final String OUT_2 = "result_2";
    private static final String OUT_3 = "result_3";
    private static final String OUT_4 = "result_4";

    @SneakyThrows
    @Override
    public void run() {
        super.run();
        log.info("执行平台api：表达式运算");
        AutoTestContext context = AutoTestContextService.getContext();
        AutoTestVariables caseVariables = context.getCaseVariables();

        List<String> argList = new ArrayList<>();
        argList.add(ARG_0);
        argList.add(ARG_1);
        argList.add(ARG_2);
        argList.add(ARG_3);
        argList.add(ARG_4);

        List<String> outList = new ArrayList<>();
        outList.add(OUT_0);
        outList.add(OUT_1);
        outList.add(OUT_2);
        outList.add(OUT_3);
        outList.add(OUT_4);

        for (int i = 0; i < argList.size(); i++) {
            String expression = getContent(argList.get(i), caseVariables);
            if(StringUtils.isNotBlank(expression)) {
                addResultInfo("表达式: ").addResultInfoLine(expression);
                String result = ExpressionUtils.executeExpression(expression, caseVariables.getVariables());
                addResultInfo("表达式结果: ").addResultInfoLine(result);
                putResultVariable(outList.get(i), result);
            }
        }
    }
}
