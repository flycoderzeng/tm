package com.tm.common.base.mapper;

import com.tm.common.base.model.ApiIpPortConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApiIpPortConfigMapper {
    List<ApiIpPortConfig> selectByUrlAndEnvId(@Param("url") String url, @Param("envId") Integer envId);
}
