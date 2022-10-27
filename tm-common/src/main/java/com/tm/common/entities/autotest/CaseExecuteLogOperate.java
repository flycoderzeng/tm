package com.tm.common.entities.autotest;

import com.tm.common.entities.autotest.enumerate.LogOperateTypeEnum;
import lombok.Data;

@Data
public class CaseExecuteLogOperate {
    private LogOperateTypeEnum logOperateTypeEnum;
    private Object logRow;


    public CaseExecuteLogOperate(LogOperateTypeEnum logOperateTypeEnum, Object logRow) {
        this.logOperateTypeEnum = logOperateTypeEnum;
        this.logRow = logRow;
    }
}
