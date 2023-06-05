package com.tm.common.base.model;

import lombok.Data;

@Data
public class AutoCase extends DataNode {
    private Integer id;
    private Integer type;
    private String steps;
    private Integer lastRunEnvId;
    private String groupVariables;
}
