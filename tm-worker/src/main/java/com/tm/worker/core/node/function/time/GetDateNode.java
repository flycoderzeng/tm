package com.tm.worker.core.node.function.time;


import com.tm.common.entities.autotest.enumerate.DateUnitTypeNum;
import com.tm.common.utils.DateUtils;
import com.tm.worker.core.exception.TMException;
import com.tm.worker.core.node.function.FunctionNode;
import com.tm.worker.core.threads.AutoTestContext;
import com.tm.worker.core.threads.AutoTestContextService;
import com.tm.worker.core.variable.AutoTestVariables;
import com.tm.worker.utils.ExpressionUtils;
import com.tm.worker.utils.FunctionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

import static com.tm.common.entities.autotest.enumerate.DateUnitTypeNum.DAY;

@Slf4j
public class GetDateNode extends FunctionNode {
    private static final String ARG_1 = "format";
    private static final String ARG_2 = "offset";
    private static final String ARG_3 = "unit";
    private static final String ARG_4 = "baseTime";

    @Override
    public void run() throws Exception {
        super.run();
        log.info("执行平台api：获取时间");
        AutoTestContext context = AutoTestContextService.getContext();
        AutoTestVariables caseVariables = context.getCaseVariables();
        String format = "";
        if (!parametersMap.containsKey(ARG_1)) {
            format = DateUtils.DATE_PATTERN_DEFAULT;
        }else{
            format = parametersMap.get(ARG_1);
            format = ExpressionUtils.replaceExpression(format, caseVariables.getVariables());
            if(StringUtils.isBlank(format)) {
                format = DateUtils.DATE_PATTERN_DEFAULT;
            }
        }
        addResultInfo("format: ").addResultInfoLine(format);

        Integer offset = 0;
        if(parametersMap.containsKey(ARG_2)) {
            String offsetString = ExpressionUtils.replaceExpression(parametersMap.get(ARG_2), caseVariables.getVariables());
            offset = FunctionUtils.getIntegerFromString(offsetString, ARG_2, offset);
        }
        addResultInfo("offset: ").addResultInfoLine(offset);

        Integer unit = DAY.val();
        if(parametersMap.containsKey(ARG_3)) {
            String unitString = ExpressionUtils.replaceExpression(parametersMap.get(ARG_3), caseVariables.getVariables());
            if(StringUtils.isNotBlank(unitString) && !StringUtils.isNumeric(unitString)) {
                throw new TMException("[" + ARG_3 + "]参数值错误。必须是：1-秒；2-分；3-时；4-日；5-月；6-年。");
            }
            if(StringUtils.isNotBlank(unitString)) {
                unit = Integer.valueOf(unitString);
            }
        }
        addResultInfo("unit: ").addResultInfoLine(unit);

        String baseTime = "";
        if(parametersMap.containsKey(ARG_4)) {
            baseTime = parametersMap.get(ARG_4);
            baseTime = ExpressionUtils.replaceExpression(baseTime, caseVariables.getVariables());
        }
        addResultInfo("baseTime: ").addResultInfoLine(baseTime);

        Date baseDate = DateUtils.stringDateToDate(baseTime);
        DateUnitTypeNum unitTypeNum = DateUnitTypeNum.get(unit);

        Date resultDate = DateUtils.addOffset(baseDate, unitTypeNum, offset);

        String resultDateString = DateUtils.parseTimestampToFormatDate(resultDate.getTime(), format);
        log.info("生成的结果是: {}", resultDateString);
        addResultInfo(arg100).addResultInfo(": ").addResultInfoLine(resultDateString);

        putResultVariable(arg100, resultDateString);
        putResultVariable(arg101, resultDate.getTime() + "");
        putResultVariable(arg102, (resultDate.getTime()/1000) + "");
    }
}
