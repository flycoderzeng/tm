package com.tm.worker.core.node.function.randomizer;

import com.googlecode.aviator.AviatorEvaluator;
import com.tm.common.utils.DateUtils;
import com.tm.worker.core.exception.TMException;
import com.tm.worker.core.node.function.FunctionNode;
import com.tm.worker.core.threads.AutoTestContext;
import com.tm.worker.core.threads.AutoTestContextService;
import com.tm.worker.core.variable.AutoTestVariables;
import com.tm.worker.utils.ExpressionUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


@Slf4j
@Data
public class GetRandomIntNode extends FunctionNode {
    private static final String ARG_1 = "count";
    private static final String ARG_2 = "prefix";
    private static final String ARG_3 = "suffix";

    @Override
    public void run() {
        super.run();
        log.info("执行平台api：生成指定位数的随机数");
        AutoTestContext context = AutoTestContextService.getContext();
        AutoTestVariables caseVariables = context.getCaseVariables();
        if (!parametersMap.containsKey(ARG_1)) {
            throw new TMException("缺少必要的参数: [" + ARG_1 + "]");
        }
        String count = parametersMap.get(ARG_1);
        count = ExpressionUtils.replaceExpression(count, caseVariables.getVariables());
        if (!StringUtils.isNumeric(count)) {
            throw new TMException("[" + ARG_1 + "]参数值类型错误，必须是数字。当前的值是：" + count);
        }

        addResultInfo("count: ").addResultInfoLine(count);

        String prefix = "";
        if (parametersMap.containsKey(ARG_2)) {
            prefix = parametersMap.get(ARG_2);
        }
        prefix = ExpressionUtils.replaceExpression(prefix, caseVariables.getVariables());
        addResultInfo("prefix: ").addResultInfoLine(prefix);

        String suffix = "";
        if (parametersMap.containsKey(ARG_3)) {
            suffix = parametersMap.get(ARG_3);
        }
        suffix = ExpressionUtils.replaceExpression(suffix, caseVariables.getVariables());
        addResultInfo("suffix: ").addResultInfoLine(suffix);

        String format = String.format("__getRandomInt(%s, '%s', '%s')", count, prefix, suffix);
        addResultInfo("format: ").addResultInfoLine(format);
        log.info("执行的函数表达式是：{}", format);
        Object execute = AviatorEvaluator.execute(format);
        log.info("生成的结果是: {}", execute);
        addResultInfo(arg100).addResultInfo(": ").addResultInfoLine(execute.toString());

        putResultVariable(arg100, (String) execute);
        putResultVariable(arg101, DateUtils.parseTimestampToFormatDate(System.currentTimeMillis(), DateUtils.DATE_PATTERN_YMD) + execute);
        putResultVariable(arg102, DateUtils.parseTimestampToFormatDate(System.currentTimeMillis(), DateUtils.DATE_PATTERN_YMDHMS) + execute);
    }
}
