package com.tm.common.base.mapper;

import com.tm.common.base.model.Menu;
import com.tm.common.entities.base.CommonTableQueryBody;

import java.util.List;

public interface MenuMapper {
    List<Menu> selectMenuTree(int parentId);

    int deleteByPrimaryKey(int id);

    Menu findById(int id);

    List<Menu> queryList(CommonTableQueryBody body);

    int countList(CommonTableQueryBody body);

    int updateBySelective(Menu record);

    int insertBySelective(Menu record);
}
