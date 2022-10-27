package com.tm.common.base.mapper;

import com.tm.common.base.model.AutoScript;

public interface AutoScriptMapper {
    int insertBySelective(AutoScript record);
    int updateBySelective(AutoScript record);
    AutoScript selectByPrimaryId(Integer id);
}
