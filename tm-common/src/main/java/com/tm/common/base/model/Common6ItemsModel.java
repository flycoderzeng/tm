package com.tm.common.base.model;

import lombok.Data;

@Data
public class Common6ItemsModel extends Common5ItemsModel {
    // 0-正常 1-删除
    private Integer status;
}
