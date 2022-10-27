package com.tm.common.base.model;

import lombok.Data;

import java.util.Date;

@Data
public class Common5ItemsModel {
    private Integer id;
    private Date addTime;
    private String addUser;
    private Date lastModifyTime;
    private String lastModifyUser;
}
