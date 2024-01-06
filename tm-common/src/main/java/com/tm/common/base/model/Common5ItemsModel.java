package com.tm.common.base.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Common5ItemsModel implements Serializable {
    private Integer id;
    private Date addTime;
    private String addUser;
    private Date lastModifyTime;
    private String lastModifyUser;
}
