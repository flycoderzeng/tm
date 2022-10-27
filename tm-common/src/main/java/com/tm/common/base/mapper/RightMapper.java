package com.tm.common.base.mapper;

import com.tm.common.base.model.Right;
import com.tm.common.entities.base.CommonTableQueryBody;

import java.util.List;

public interface RightMapper {
    List<Right> queryList(CommonTableQueryBody body);
    int countList(CommonTableQueryBody body);
    Right findById(Integer id);
    Right findByURI(String uri);
    int updateBySelective(Right right);
    int insertBySelective(Right right);
    int deleteByPrimaryKey(int id);
}
