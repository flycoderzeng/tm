package com.tm.common.base.model;


import lombok.Data;

import java.util.List;

@Data
public class ProjectUserRelation extends ProjectUser {
    private String username;
    private String chineseName;
    private List<ProjectUserRole> roleList;
}
