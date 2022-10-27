package com.tm.common.base.model;

import lombok.Data;

import java.util.Date;


@Data
public class ProjectUserRole extends Common5ItemsModel{
    private Integer projectId;
    private Integer userId;
    private Integer roleId;
    private String roleChineseName;
}
