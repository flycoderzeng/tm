package com.tm.worker.core.node.function.randomizer;

import cn.hutool.core.util.StrUtil;
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
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;


@Slf4j
@Data
public class GetRandomIntNode extends FunctionNode {
    private static final String ARG_1 = "count";
    private static final String ARG_2 = "prefix";
    private static final String ARG_3 = "suffix";
    private static final String ARG_4 = "minInclude";
    private static final String ARG_5 = "maxInclude";

    @Override
    public void run() throws Exception {
        super.run();
        log.info("执行平台api：生成指定位数的随机数");
        AutoTestContext context = AutoTestContextService.getContext();
        AutoTestVariables caseVariables = context.getCaseVariables();

        String minInclude = getParameterValue(ARG_4, caseVariables, "minInclude: ");
        Long min = getMinMaxIntValue(minInclude);

        String maxInclude = getParameterValue(ARG_5, caseVariables, "maxInclude: ");
        Long max = getMinMaxIntValue(maxInclude);

        String generateResult = "";

        if(min != null && max != null) {
            generateResult = RandomUtils.nextLong(min, max+1) + "";
        }else{
            String count = parametersMap.get(ARG_1);
            count = ExpressionUtils.replaceExpression(count, caseVariables.getVariables());
            if(StringUtils.isBlank(count)) {
                count = "1";
            }
            if (!StringUtils.isNumeric(count)) {
                throw new TMException("[" + ARG_1 + "]参数值类型错误，必须是数字。当前的值是：" + count);
            }
            addResultInfo("count: ").addResultInfoLine(count);
            Integer intLen = Integer.valueOf(count);
            generateResult = RandomStringUtils.randomNumeric(intLen);
        }

        String prefix = getParameterValue(ARG_2, caseVariables, "prefix: ");
        String suffix = getParameterValue(ARG_3, caseVariables, "suffix: ");
        if(StringUtils.isNoneBlank(prefix)) {
            generateResult = prefix + generateResult;
        }
        if(StringUtils.isNoneBlank(suffix)) {
            generateResult = generateResult + suffix;
        }

        log.info("生成的结果是: {}", generateResult);
        addResultInfo(arg100).addResultInfo(": ").addResultInfoLine(generateResult);

        putResultVariable(arg100, generateResult);
        putResultVariable(arg101, DateUtils.parseTimestampToFormatDate(System.currentTimeMillis(), DateUtils.DATE_PATTERN_YMD) + generateResult);
        putResultVariable(arg102, DateUtils.parseTimestampToFormatDate(System.currentTimeMillis(), DateUtils.DATE_PATTERN_YMDHMS) + generateResult);
    }

    private Long getMinMaxIntValue(String valueInclude) {
        Long value = null;
        if(StringUtils.isNoneBlank(valueInclude)) {
            boolean isNegative = false;
            if (valueInclude.startsWith("-")) {
                isNegative = true;
                valueInclude = valueInclude.substring(1);
            }
            if (!StrUtil.isNumeric(valueInclude)) {
                throw new TMException("最小值不是数字");
            }
            value = Long.valueOf(valueInclude);
            if(isNegative) {
                value = value * -1;
            }
        }
        return value;
    }

    private String getParameterValue(String argName, AutoTestVariables caseVariables, String info) {
        String value = "";
        if (parametersMap.containsKey(argName)) {
            value = parametersMap.get(argName);
        }
        value = ExpressionUtils.replaceExpression(value, caseVariables.getVariables());
        addResultInfo(info).addResultInfoLine(value);
        return value;
    }
}
