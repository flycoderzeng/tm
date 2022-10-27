package com.tm.common.entities.common.enumerate;

public enum ResultCodeEnum {
    SUCCESS(0, "success"),
    PARAM_ERROR(100001, "参数错误"),
    LOGIN_ERROR(100002, "用户名或密码错误"),
    NODE_TREE_LEVEL_OVERFLOW_ERROR(100003, "不能超过10层"),
    COPY_NODE_OVERFLOW_ERROR(100004, "复制的节点数超出限制"),
    COPY_TASK_OVERFLOW_ERROR(100005, "当前的计划任务太多，请稍后提交！"),
    CASE_RUN_ERROR(100006, "用例执行失败"),
    SYSTEM_ERROR(999999, "系统错误"),
    ;
    private Integer code;

    private String msg;

    ResultCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
