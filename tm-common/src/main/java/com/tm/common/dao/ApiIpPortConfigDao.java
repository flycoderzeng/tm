package com.tm.common.dao;

import com.tm.common.base.mapper.ApiIpPortConfigMapper;
import com.tm.common.base.model.ApiIpPortConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiIpPortConfigDao {
    @Autowired
    private ApiIpPortConfigMapper apiIpPortConfigMapper;

    public List<ApiIpPortConfig> selectByUrlAndEnvId(@Param("url") String url, @Param("envId") Integer envId) {
        return apiIpPortConfigMapper.selectByUrlAndEnvId(url, envId);
    }
}
