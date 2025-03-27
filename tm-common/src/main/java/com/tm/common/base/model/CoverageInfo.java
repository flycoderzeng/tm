package com.tm.common.base.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CoverageInfo extends Common6ItemsModel {
    private String name;
    private String gitUrl;
    private String branch;
    private String branch2;
    private String gitCommitId1;
    private String gitCommitId2;
    private Integer missedInstructions;
    private Integer coveredInstructions;
    private Integer missedBranches;
    private Integer coveredBranches;
    private Integer missedCxty;
    private Integer coveredCxty;
    private Integer missedMethods;
    private Integer coveredMethods;
    private Integer missedClasses;
    private Integer coveredClasses;
    private Integer missedLines;
    private Integer coveredLines;
    private byte[] coverageInfo;
}
