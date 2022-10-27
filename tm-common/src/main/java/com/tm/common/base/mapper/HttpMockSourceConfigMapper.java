package com.tm.common.base.mapper;

import com.tm.common.base.model.HttpMockSourceConfig;
import com.tm.common.entities.base.CommonTableQueryBody;

import java.util.List;

public interface HttpMockSourceConfigMapper {
    int deleteByPrimaryKey(int id);
    int insertBySelective(HttpMockSourceConfig record);
    int updateBySelective(HttpMockSourceConfig record);
    HttpMockSourceConfig selectByPrimaryId(Integer id);

    List<HttpMockSourceConfig> queryList(CommonTableQueryBody body);

    int countList(CommonTableQueryBody body);
}
