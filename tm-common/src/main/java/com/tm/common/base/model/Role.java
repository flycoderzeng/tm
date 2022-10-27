package com.tm.common.base.model;

import lombok.Data;

import java.io.Serializable;


@Data
public class Role extends Common6ItemsModel  implements Serializable {
    private String name;
    private String description;
    private String chineseName;
    private Integer type;
}
