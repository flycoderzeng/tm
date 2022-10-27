package com.tm.common.base.model;


import lombok.Data;

@Data
public class GlobalVariable extends CommonNameDescriptionModel {
    private Integer id;
    private String value = "";
    private Integer modifyFlag = 1;
}
