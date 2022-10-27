package com.tm.common.base.mapper;

import com.tm.common.base.model.ApiIpPortConfig;
import com.tm.common.entities.base.CommonTableQueryBody;

import java.util.List;

public interface ApiIpPortConfigMapper {
    int deleteByPrimaryKey(int id);

    ApiIpPortConfig findById(int id);

    List<ApiIpPortConfig> queryList(CommonTableQueryBody body);

    int countList(CommonTableQueryBody body);

    int updateBySelective(ApiIpPortConfig record);

    int insertBySelective(ApiIpPortConfig record);
}
