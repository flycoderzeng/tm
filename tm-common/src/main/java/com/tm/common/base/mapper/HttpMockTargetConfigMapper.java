package com.tm.common.base.mapper;

import com.tm.common.base.model.HttpMockTargetConfig;
import com.tm.common.entities.base.CommonTableQueryBody;

import java.util.List;

public interface HttpMockTargetConfigMapper {
    int deleteByPrimaryKey(int id);
    int insertBySelective(HttpMockTargetConfig record);
    int updateBySelective(HttpMockTargetConfig record);
    HttpMockTargetConfig selectByPrimaryId(Integer id);

    List<HttpMockTargetConfig> queryList(CommonTableQueryBody body);

    int countList(CommonTableQueryBody body);
}
