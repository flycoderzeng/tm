package com.tm.worker.core.node.function.gvariables;

import com.tm.common.base.model.DataNode;
import com.tm.common.base.model.GlobalVariable;
import com.tm.common.entities.common.enumerate.DataTypeEnum;
import com.tm.worker.core.exception.TMException;
import com.tm.worker.core.node.function.FunctionNode;
import com.tm.worker.core.threads.AutoTestContext;
import com.tm.worker.core.threads.AutoTestContextService;
import com.tm.worker.core.variable.AutoTestVariables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Slf4j
public class GetGlobalKeyValueNode extends FunctionNode {
    @Override
    public void run() throws Exception {
        super.run();
        log.info("执行平台api：获取全局变量值");
        AutoTestContext context = AutoTestContextService.getContext();
        AutoTestVariables caseVariables = context.getCaseVariables();
        String globalKeyName = getContent("globalKeyName", caseVariables);
        if(StringUtils.isBlank(globalKeyName)) {
            throw new TMException("全局变量名称不能为空!");
        }

        List<DataNode> dataNodes = context.getTaskService().selectByDataTypeIdAndName(DataTypeEnum.GLOBAL_VARIABLE.value(), globalKeyName);
        if(dataNodes == null || dataNodes.isEmpty()) {
            throw new TMException("没有找到全局变量: " + globalKeyName);
        }
        if(dataNodes.size() > 1) {
            throw new TMException("找到多个全局变量: " + globalKeyName);
        }
        DataNode dataNode = dataNodes.get(0);
        GlobalVariable globalVariable = context.getTaskService().selectGlobalVariableByPrimaryId(dataNode.getId());
        if(globalVariable == null) {
            throw new TMException("没有找到全局变量: " + dataNode.getId());
        }
        putResultVariable("result", globalVariable.getValue());
    }
}
