package com.tm.common.base.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AutoPlan extends DataNode {
    private Integer id;
    private Integer type = 1;
    private String mailList;
    private Integer maxConcurrent;
    private Integer runs;
    private Integer envId;
    private Integer maxOccurs;
    private Integer maxFailRetry;
    private Integer failContinue;
    private String planVariables;
}
