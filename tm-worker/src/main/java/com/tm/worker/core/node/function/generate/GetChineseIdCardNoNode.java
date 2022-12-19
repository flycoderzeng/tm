package com.tm.worker.core.node.function.generate;

import cn.binarywang.tools.generator.ChineseIDCardNumberGenerator;
import com.tm.common.utils.DateUtils;
import com.tm.worker.core.node.function.FunctionNode;
import com.tm.worker.core.threads.AutoTestContext;
import com.tm.worker.core.threads.AutoTestContextService;
import com.tm.worker.core.variable.AutoTestVariables;
import com.tm.worker.utils.ExpressionUtils;
import com.tm.worker.utils.FunctionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Slf4j
public class GetChineseIdCardNoNode extends FunctionNode {
    private static final String ARG_1 = "age";
    private static final String ARG_2 = "sex";
    private static final String ARG_3 = "birthday";
    private static final String ARG_4 = "baseDate";

    @Override
    public void run() throws Exception {
        super.run();
        log.info("执行平台api：获取中国idcardno");
        AutoTestContext context = AutoTestContextService.getContext();
        AutoTestVariables caseVariables = context.getCaseVariables();

        String result = ChineseIDCardNumberGenerator.getInstance().generate();
        log.info("生成中国idcardno的结果是：{}", result);
        String issueOrg = ChineseIDCardNumberGenerator.generateIssueOrg();
        log.info("生成中国开卡公安局的结果是：{}", issueOrg);
        String period = ChineseIDCardNumberGenerator.generateValidPeriod();
        log.info("生成的有效期结果是：{}", period);

        Integer age = null;
        if(parametersMap.containsKey(ARG_1)) {
            String ageString = ExpressionUtils.replaceExpression(parametersMap.get(ARG_1), caseVariables.getVariables());
            age = FunctionUtils.getIntegerFromString(ageString, ARG_1, age);
        }
        addResultInfo("age: ").addResultInfoLine(age);

        Integer sex = null;
        if(parametersMap.containsKey(ARG_2)) {
            String ageString = ExpressionUtils.replaceExpression(parametersMap.get(ARG_2), caseVariables.getVariables());
            sex = FunctionUtils.getIntegerFromString(ageString, ARG_2, sex);
        }
        addResultInfo("sex: ").addResultInfoLine(sex);

        String birthDay = null;
        if(parametersMap.containsKey(ARG_3)) {
            birthDay = ExpressionUtils.replaceExpression(parametersMap.get(ARG_3), caseVariables.getVariables());
        }
        addResultInfo("birthDay: ").addResultInfoLine(birthDay);

        String baseDateString = null;
        if(parametersMap.containsKey(ARG_4)) {
            baseDateString = ExpressionUtils.replaceExpression(parametersMap.get(ARG_4), caseVariables.getVariables());
        }
        addResultInfo("baseDate: ").addResultInfoLine(baseDateString);

        // 如果指定了生日
        if(StringUtils.isNotBlank(birthDay)) {
            result = result.substring(0, 6) + birthDay + result.substring(14);
        }
        // 如果第17位数字是奇数，则代表男性，是偶数则代表女性。
        // 性别设置为女
        if(sex != null && sex.equals(1)) {
            result = result.substring(0, 17) + FunctionUtils.randomUnitEvenNumber() + result.substring(17);
        }else if(sex != null) { //性别设置为男
            result = result.substring(0, 17) + FunctionUtils.randomUnitOddNumber() + result.substring(17);
        }

        // 如果没有指定生日， 并且指定了年龄
        if(StringUtils.isBlank(birthDay) && age != null) {
            Date baseDate = new Date();
            if(StringUtils.isNotBlank(baseDateString)) {
                baseDate = DateUtils.stringDateToDate(baseDateString);
            }
            Date date = DateUtils.addYears(baseDate, 0 - age);
            String ymd = DateUtils.parseTimestampToFormatDate(date.getTime(), DateUtils.DATE_PATTERN_YMD);
            result = result.substring(0, 6) + ymd + result.substring(14);
        }

        addResultInfo("身份证号码: ").addResultInfoLine(result);
        addResultInfo("开卡公安局: ").addResultInfoLine(issueOrg);
        addResultInfo("有效期: ").addResultInfoLine(period);
        putResultVariable(arg100, result);
        putResultVariable(arg101, issueOrg);
        putResultVariable(arg102, period);
    }
}

