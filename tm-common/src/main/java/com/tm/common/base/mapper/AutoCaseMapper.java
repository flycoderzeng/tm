package com.tm.common.base.mapper;

import com.tm.common.base.model.AutoCase;

public interface AutoCaseMapper {
    int insertBySelective(AutoCase record);
    int updateBySelective(AutoCase record);
    AutoCase selectByPrimaryId(Integer id);
}
