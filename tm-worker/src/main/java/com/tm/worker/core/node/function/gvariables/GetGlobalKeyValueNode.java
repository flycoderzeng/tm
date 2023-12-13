package com.tm.worker.core.node.function.gvariables;

import com.tm.common.base.model.GlobalVariable;
import com.tm.worker.core.node.function.FunctionNode;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class GetGlobalKeyValueNode extends FunctionNode {
    @Override
    public void run() throws Exception {
        super.run();
        log.info("执行平台api：获取全局变量值");
        GlobalVariable globalVariable = getGlobalVariable();
        putResultVariable("result", globalVariable.getValue());
    }
}
