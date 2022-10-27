package com.tm.worker.core.node.function.time;

import com.tm.worker.core.node.function.FunctionNode;
import com.tm.worker.core.threads.AutoTestContext;
import com.tm.worker.core.threads.AutoTestContextService;
import com.tm.worker.core.variable.AutoTestVariables;
import com.tm.worker.utils.ExpressionUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetTimestampNode extends FunctionNode {
    private static final String ARG_1 = "type";

    @Override
    public void run() {
        super.run();
        log.info("执行平台api：获取时间戳");
        AutoTestContext context = AutoTestContextService.getContext();
        AutoTestVariables caseVariables = context.getCaseVariables();
        String type = "0";
        if (parametersMap.containsKey(ARG_1)) {
            type = parametersMap.get(ARG_1);
            type = ExpressionUtils.replaceExpression(type, caseVariables.getVariables());
        }
        addResultInfo("type: ").addResultInfoLine(type);
        String resultDateString = System.currentTimeMillis() + "";
        if(!"0".equals(type)) {
            resultDateString = resultDateString.substring(0, 10);
        }
        log.info("生成的结果是: {}", resultDateString);
        addResultInfo(arg100).addResultInfo(": ").addResultInfoLine(resultDateString);

        putResultVariable(arg100, resultDateString);
    }
}
