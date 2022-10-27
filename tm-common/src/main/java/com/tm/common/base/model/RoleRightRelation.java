package com.tm.common.base.model;

import lombok.Data;


@Data
public class RoleRightRelation extends RoleRight {
    private String rightName;
    private String uri;
    private Integer rightType;
}
