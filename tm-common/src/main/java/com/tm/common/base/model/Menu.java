package com.tm.common.base.model;

import lombok.Data;

import java.util.List;

@Data
public class Menu extends Common6ItemsModel {
    private String menuName;

    private String url;

    private Integer parentId;

    private String icon;

    private Integer seq;

    private Integer level;

    private Integer pageId;

    private List<Menu> children;
}
