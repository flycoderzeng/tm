package com.tm.common.base.model;

import lombok.Data;

import java.util.Date;

@Data
public class AutoCaseHistory {
    private Integer id;
    private Integer autoCaseId;
    private String steps;
    private String groupVariables;
    private Date addTime;
    private String addUser;
}
