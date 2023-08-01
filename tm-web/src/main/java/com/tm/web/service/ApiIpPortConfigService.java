package com.tm.web.service;

import com.tm.common.base.mapper.ApiIpPortConfigMapper;
import com.tm.common.base.mapper.RunEnvMapper;
import com.tm.common.base.model.ApiIpPortConfig;
import com.tm.common.base.model.DbConfig;
import com.tm.common.base.model.RunEnv;
import com.tm.common.base.model.User;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.common.enumerate.ResultCodeEnum;
import com.tm.common.entities.testmanage.BatchCopyCommonConfigBody;
import com.tm.common.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service("apiIpPortConfigService")
public class ApiIpPortConfigService {
    @Autowired
    private ApiIpPortConfigMapper apiIpPortConfigMapper;
    @Autowired
    private RunEnvMapper runEnvMapper;

    public BaseResponse batchCopyApiIpPortConfig(BatchCopyCommonConfigBody body, User loginUser) {
        final List<ApiIpPortConfig> apiIpPortConfigs = apiIpPortConfigMapper.selectConfigByEnvId(body.getSrcEnvId());
        if(apiIpPortConfigs == null || apiIpPortConfigs.isEmpty()) {
            return ResultUtils.error(ResultCodeEnum.SOURCE_ENV_RELATIVE_CONFIG_EMPTY);
        }
        final RunEnv runEnv = runEnvMapper.findById(body.getDesEnvId());
        if(runEnv == null) {
            return ResultUtils.error(ResultCodeEnum.SYSTEM_ERROR);
        }

        final List<ApiIpPortConfig> desApiIpPortConfigs = apiIpPortConfigMapper.selectConfigByEnvId(runEnv.getId());

        List<ApiIpPortConfig> newList = new ArrayList<>();

        for (ApiIpPortConfig apiIpPortConfig : apiIpPortConfigs) {
            boolean existed = false;
            if(desApiIpPortConfigs != null && !desApiIpPortConfigs.isEmpty()) {
                for (ApiIpPortConfig desApiIpPortConfig : desApiIpPortConfigs) {
                    if(StringUtils.equals(apiIpPortConfig.getUrl(), desApiIpPortConfig.getUrl())) {
                        existed = true;
                        break;
                    }
                }
            }
            if(!existed) {
                apiIpPortConfig.setId(null);
                apiIpPortConfig.setEnvId(runEnv.getId());
                apiIpPortConfig.setStatus(0);
                apiIpPortConfig.setIp(body.getIp());
                apiIpPortConfig.setPort(body.getPort());
                apiIpPortConfig.setAddTime(new Date());
                apiIpPortConfig.setAddUser(loginUser.getUsername());
                newList.add(apiIpPortConfig);
            }
        }
        if(!newList.isEmpty()) {
            apiIpPortConfigMapper.batchInsert(newList);
        }

        return ResultUtils.success();
    }

    public BaseResponse setDcnIdToNull(Integer id) {
        ApiIpPortConfig apiIpPortConfig = apiIpPortConfigMapper.selectByPrimaryId(id);
        if(apiIpPortConfig == null) {
            return ResultUtils.success();
        }
        apiIpPortConfigMapper.setDcnIdToNull(id);

        return ResultUtils.success();
    }
}
