package com.tm.common.base.model;

import lombok.Data;

import java.util.Date;

@Data
public class PlanCase {
    private Integer id;
    private Integer planId;
    private Integer caseId;
    private Integer seq;
    private String caseName;
    private Date caseCreateTime;
    private String caseCreateUsername;
    private Date caseModifyTime;
    private String caseModifyUsername;

    public PlanCase() {

    }

    public PlanCase(Integer planId, Integer caseId, Integer seq) {
        this.planId = planId;
        this.caseId = caseId;
        this.seq = seq;
    }
}
