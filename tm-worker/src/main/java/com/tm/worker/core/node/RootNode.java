package com.tm.worker.core.node;

import com.tm.common.entities.common.KeyValueRow;
import com.tm.worker.core.cookie.AutoTestCookie;
import com.tm.worker.core.threads.AutoTestContext;
import com.tm.worker.core.threads.AutoTestContextService;
import com.tm.worker.core.variable.AutoCaseVariable;
import com.tm.worker.core.variable.AutoTestVariables;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
public class RootNode extends StepNodeBase {
    private List<AutoCaseVariable> userDefinedVariables;
    private List<KeyValueRow> cookies;
    @Override
    public void run() throws Exception {
        super.run();
        AutoTestContext autoTestContext = AutoTestContextService.getContext();
        AutoTestVariables caseVariables = autoTestContext.getCaseVariables();
        // 将计划变量的值更新到用例变量
        AutoTestVariables planVariables = autoTestContext.getPlanTask().getPlanVariables();
        caseVariables.updateAutoCaseVariables(userDefinedVariables, planVariables);
        // 如果是组合方式运行，将组合变量值，初始化为用例变量值
        AutoTestVariables groupVariables = autoTestContext.getCaseTask().getGroupVariables();
        if(groupVariables != null) {
            caseVariables.replace(groupVariables);
        }
        caseVariables.execBuiltinFunction();

        autoTestContext.setAutoTestCookie(new AutoTestCookie(cookies));
    }
}
