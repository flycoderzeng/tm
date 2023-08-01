package com.tm.common.base.mapper;

import com.tm.common.base.model.ApiIpPortConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApiIpPortConfigMapper {
    List<ApiIpPortConfig> selectConfigByUrlEnvIdAndDcnId(@Param("url") String url, @Param("envId") Integer envId, @Param("dcnId") Integer dcnId);
    List<ApiIpPortConfig> selectConfigByEnvId(@Param("envId") Integer envId);
    ApiIpPortConfig selectByPrimaryId(Integer id);
    int setDcnIdToNull(Integer id);
    int batchInsert(@Param("apiIpPortConfigs") List<ApiIpPortConfig> apiIpPortConfigs);
}
