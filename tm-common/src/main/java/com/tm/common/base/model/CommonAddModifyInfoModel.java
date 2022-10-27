package com.tm.common.base.model;

import lombok.Data;

import java.util.Date;


@Data
public class CommonAddModifyInfoModel extends CommonNameDescriptionModel {
    private String addUser;
    private Date addTime;
    private String lastModifyUser;
    private Date lastModifyTime;
}
