package com.tm.common.base.mapper;

import com.tm.common.base.model.GlobalVariable;

public interface GlobalVariableMapper {
    int insertBySelective(GlobalVariable record);
    int updateBySelective(GlobalVariable record);
    GlobalVariable selectByPrimaryId(Integer id);
}
