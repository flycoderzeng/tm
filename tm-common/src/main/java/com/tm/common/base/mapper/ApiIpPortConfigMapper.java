package com.tm.common.base.mapper;

import com.tm.common.base.model.ApiIpPortConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApiIpPortConfigMapper {
    List<ApiIpPortConfig> selectByUrlAndEnvId(@Param("url") String url, @Param("envId") Integer envId);
    List<ApiIpPortConfig> selectByEnvId(@Param("envId") Integer envId);
    int batchInsert(@Param("apiIpPortConfigs") List<ApiIpPortConfig> apiIpPortConfigs);
}
