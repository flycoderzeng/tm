package com.tm.common.base.mapper;

import com.tm.common.base.model.Menu;
import com.tm.common.entities.base.CommonTableQueryBody;

import java.util.List;

public interface MenuMapper {
    List<Menu> selectMenuTree(int parentId);
}
