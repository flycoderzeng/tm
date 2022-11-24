package com.tm.common.base.mapper;

import com.tm.common.base.model.Menu;

import java.util.List;

public interface MenuMapper {
    List<Menu> selectMenuTree(int parentId);
}
