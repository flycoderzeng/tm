package com.tm.common.entities.autotest.enumerate;


//用例结果状态 1-执行中 2-执行成功（没有发生异常，断言都成功） 200-执行失败（发生异常或断言失败） 190-执行超时，强制结束 100-用户停止
public enum CaseExecuteResultStatusEnum {
    RUNNING(1),SUCCESS(2),FAIL(200),TIMEOUT(190),CANCELED(100);
    int value;
    CaseExecuteResultStatusEnum(int value) {this.value = value;}
    CaseExecuteResultStatusEnum() {}
    public int value() {return value;}
    public static CaseExecuteResultStatusEnum get(int value) {
        switch (value) {
            case 1:
                return CaseExecuteResultStatusEnum.RUNNING;
            case 2:
                return CaseExecuteResultStatusEnum.SUCCESS;
            case 3:
                return CaseExecuteResultStatusEnum.FAIL;
            case 4:
                return CaseExecuteResultStatusEnum.TIMEOUT;
            case 5:
                return CaseExecuteResultStatusEnum.CANCELED;
            default:
                return null;
        }
    }
}
