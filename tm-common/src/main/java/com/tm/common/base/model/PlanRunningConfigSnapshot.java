package com.tm.common.base.model;

import lombok.Data;

@Data
public class PlanRunningConfigSnapshot {
    private Integer id;
    private Integer planResultId;
    // 1-非组合 2-组合
    private Integer runType;
    private Integer runs = 1;
    private Integer envId;
    private String envName;
    private Integer failContinue = 1;
    private String globalVariables;
    private Integer maxOccurs = 10;
}
