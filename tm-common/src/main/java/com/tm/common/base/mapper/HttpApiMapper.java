package com.tm.common.base.mapper;

import com.tm.common.base.model.HttpApi;

public interface HttpApiMapper {
    int insertBySelective(HttpApi record);
    int updateBySelective(HttpApi record);
    HttpApi selectByPrimaryId(Integer id);
}
