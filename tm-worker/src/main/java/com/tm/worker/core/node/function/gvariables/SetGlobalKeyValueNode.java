package com.tm.worker.core.node.function.gvariables;

import com.tm.common.base.model.GlobalVariable;
import com.tm.worker.core.exception.TMException;
import com.tm.worker.core.node.function.FunctionNode;
import com.tm.worker.core.threads.AutoTestContext;
import com.tm.worker.core.threads.AutoTestContextService;
import com.tm.worker.core.variable.AutoTestVariables;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SetGlobalKeyValueNode extends FunctionNode {
    @Override
    public void run() throws Exception {
        super.run();
        log.info("执行平台api：设置全局变量");
        AutoTestContext context = AutoTestContextService.getContext();
        AutoTestVariables caseVariables = context.getCaseVariables();
        GlobalVariable globalVariable = getGlobalVariable();

        String globalKeyValue = getContent("globalKeyValue", caseVariables);
        addResultInfo("全局变量值: ").addResultInfoLine(globalKeyValue);

        if(0 == globalVariable.getModifyFlag()) {
            throw new TMException("该全局变量的配置是用例运行时不允许修改");
        }

        globalVariable.setValue(globalKeyValue);
        context.getTaskService().updateGlobalVariable(globalVariable);
    }
}
