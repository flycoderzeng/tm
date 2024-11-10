package com.tm.common.base.model;

import lombok.Data;

@Data
public class ProjectUser extends Common5ItemsModel {
    private Integer projectId;
    private Integer userId;
}
