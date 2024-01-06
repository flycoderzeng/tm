package com.tm.common.entities.autotest;

import com.tm.common.entities.autotest.enumerate.LogOperateTypeEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class CaseExecuteLogOperate implements Serializable {
    private LogOperateTypeEnum logOperateTypeEnum;
    private Object logRow;


    public CaseExecuteLogOperate(LogOperateTypeEnum logOperateTypeEnum, Object logRow) {
        this.logOperateTypeEnum = logOperateTypeEnum;
        this.logRow = logRow;
    }
}
