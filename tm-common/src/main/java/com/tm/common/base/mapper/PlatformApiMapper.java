package com.tm.common.base.mapper;

import com.tm.common.base.model.PlatformApi;

public interface PlatformApiMapper {
    int insertBySelective(PlatformApi record);
    int updateBySelective(PlatformApi record);
    PlatformApi selectByPrimaryId(Integer id);
}
