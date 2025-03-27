package com.tm.common.base.mapper;

import com.tm.common.base.model.CoverageInfo;

public interface CoverageInfoMapper {
    int insertBySelective(CoverageInfo record);
    int updateBySelective(CoverageInfo record);
    CoverageInfo selectByPrimaryId(Integer id);
    CoverageInfo selectByPrimaryId2(Integer id);
}
