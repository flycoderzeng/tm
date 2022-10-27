package com.tm.common.base.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class User extends Common6ItemsModel implements Serializable {

    private String username;

    private String chineseName;

    private String password;

    private List userRoleList;

    private String orgFullName;

    private String position;

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", chineseName='" + chineseName + '\'' +
                '}';
    }
}