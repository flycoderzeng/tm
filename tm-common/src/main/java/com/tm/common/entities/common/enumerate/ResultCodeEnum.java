package com.tm.common.entities.common.enumerate;

public enum ResultCodeEnum {
    SUCCESS(0, "success"),
    PARAM_ERROR(100001, "参数错误"),
    LOGIN_ERROR(100002, "用户名或密码错误"),
    NODE_TREE_LEVEL_OVERFLOW_ERROR(100003, "不能超过10层"),
    COPY_NODE_OVERFLOW_ERROR(100004, "复制的节点数超出500的限制"),
    COPY_TASK_OVERFLOW_ERROR(100005, "当前的计划任务太多，请稍后提交！"),
    RUN_ENV_NOT_EXISTS(100006, "运行环境不存在！"),
    AUTO_PLAN_NOT_EXISTS(100007, "计划不存在！"),
    AUTO_CASE_NOT_EXISTS(100008, "用例不存在！"),
    CASE_RUN_ERROR(100006, "用例执行失败"),
    SOURCE_ENV_RELATIVE_CONFIG_EMPTY(100007, "源环境相关配置为空"),
    PLAN_IS_STOPPED_NOT_RUN_CASE(100008, "计划已经停止,没有运行用例"),
    CURR_PLAN_RESULT_STATUS_NOT_PERMITTED(100009, "当前计划结果状态不允许重试失败用例"),
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
