package com.tm.common.base.model;

import lombok.Data;

@Data
public class PlanRunningConfigSnapshot {
    // 快照id
    private Integer id;
    // 计划结果id
    private Integer planResultId;
    // 1-非组合 2-组合
    private Integer runType;
    // 计划运行次数
    private Integer runs = 1;
    // 环境id
    private Integer envId;
    // 环境名称
    private String envName;
    // 1 - 用例失败继续执行后续用例 其他-否
    private Integer failContinue = 1;
    // 计划变量
    private String planVariables;
    // 最大并发数
    private Integer maxOccurs = 10;

    private RunEnv runEnv;
}
