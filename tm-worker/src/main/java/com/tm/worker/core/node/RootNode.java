package com.tm.worker.core.node;

import com.tm.common.entities.common.KeyValueRow;
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

    private AutoTestContext autoTestContext;

    @Override
    public void run() {
        autoTestContext = AutoTestContextService.getContext();
        AutoTestVariables caseVariables = autoTestContext.getCaseVariables();
        caseVariables.updateAutoCaseVariables(userDefinedVariables);
        // 如果是组合方式运行，将组合变量值，初始化为用例变量值
        AutoTestVariables groupVariables = autoTestContext.getCaseTask().getGroupVariables();
        if(groupVariables != null) {
            caseVariables.replace(groupVariables);
        }
    }
}
