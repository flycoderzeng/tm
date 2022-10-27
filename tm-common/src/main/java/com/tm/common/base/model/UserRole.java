package com.tm.common.base.model;

import lombok.Data;

import java.util.Date;


@Data
public class UserRole extends Common5ItemsModel {
    private Integer userId;
    private Integer roleId;
}
