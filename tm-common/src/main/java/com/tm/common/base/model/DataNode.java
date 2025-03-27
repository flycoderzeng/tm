package com.tm.common.base.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class DataNode extends CommonNameDescriptionModel {
    private Integer id;
    private Integer dataTypeId;
    private Integer projectId;
    private Integer isFolder;
    private Integer parentId;
    private Integer level;
    private Integer seq;
    private Date addTime;
    private Integer addUserId;
    private Date lastModifyTime;
    private Integer lastModifyUserId;
    private Integer parent1;
    private Integer parent2;
    private Integer parent3;
    private Integer parent4;
    private Integer parent5;
    private Integer parent6;
    private Integer parent7;
    private Integer parent8;
    private Integer parent9;
    private Integer parent10;
    private Integer status;
    private String addUser;
    private String lastModifyUser;
    private Integer childCasesCount;
}
