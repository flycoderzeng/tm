package com.tm.common.dao;

import com.tm.common.base.mapper.ApiIpPortConfigMapper;
import com.tm.common.base.model.ApiIpPortConfig;
import jakarta.inject.Inject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

@Component
public class ApiIpPortConfigDao {
    private final ApiIpPortConfigMapper apiIpPortConfigMapper;

    @Inject
    public ApiIpPortConfigDao(ApiIpPortConfigMapper apiIpPortConfigMapper) {
        Assert.notNull(apiIpPortConfigMapper, "ApiIpPortConfigMapper must not be null!");
        this.apiIpPortConfigMapper = apiIpPortConfigMapper;
    }

    public List<ApiIpPortConfig> selectByUrlAndEnvId(@Param("url") String url, @Param("envId") Integer envId, @Param("dcnId") Integer dcnId) {
        return apiIpPortConfigMapper.selectConfigByUrlEnvIdAndDcnId(url, envId, dcnId);
    }
}
