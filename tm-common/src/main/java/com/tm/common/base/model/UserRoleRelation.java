package com.tm.common.base.model;


import lombok.Data;

@Data
public class UserRoleRelation extends UserRole {
    private String roleName;
    private String roleDescription;
}
