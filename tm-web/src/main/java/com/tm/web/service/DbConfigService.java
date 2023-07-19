package com.tm.web.service;

import com.tm.common.base.mapper.DbConfigMapper;
import com.tm.common.base.mapper.RunEnvMapper;
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
@Service("dbConfigService")
public class DbConfigService extends BaseService {
    @Autowired
    private DbConfigMapper dbConfigMapper;
    @Autowired
    private RunEnvMapper runEnvMapper;

    public BaseResponse getAllDatabaseNames() {
        return ResultUtils.success(dbConfigMapper.getAllDatabaseNames());
    }

    public BaseResponse batchCopyDbConfig(BatchCopyCommonConfigBody body, User loginUser) {
        final List<DbConfig> dbConfigs = dbConfigMapper.findByEnvIdAndDcnId(body.getSrcEnvId(), body.getSrcDcnId());
        if(dbConfigs == null || dbConfigs.isEmpty()) {
            return ResultUtils.error(ResultCodeEnum.SOURCE_ENV_RELATIVE_CONFIG_EMPTY);
        }

        final RunEnv runEnv = runEnvMapper.findById(body.getDesEnvId());
        if(runEnv == null) {
            return ResultUtils.error(ResultCodeEnum.SYSTEM_ERROR);
        }

        final List<DbConfig> desDbConfigs = dbConfigMapper.findByEnvId(body.getDesEnvId());

        List<DbConfig> newList = new ArrayList<>();

        for (DbConfig dbConfig : dbConfigs) {
            boolean existed = false;
            if(desDbConfigs != null && !desDbConfigs.isEmpty()) {
                for (DbConfig desDbConfig : desDbConfigs) {
                    if((StringUtils.equals(dbConfig.getDbName(), desDbConfig.getDbName()) &&
                    dbConfig.getDcnId() == null && desDbConfig.getDcnId() == null) || (
                            StringUtils.equals(dbConfig.getDbName(), desDbConfig.getDbName()) &&
                                    (dbConfig.getDcnId() != null && dbConfig.getDcnId().equals(desDbConfig.getDcnId()))
                            )) {
                        existed = true;
                        break;
                    }
                }
            }
            if(!existed) {
                dbConfig.setId(null);
                dbConfig.setStatus(0);
                dbConfig.setEnvId(runEnv.getId());
                dbConfig.setIp(body.getIp());
                dbConfig.setPort(body.getPort());
                dbConfig.setAddTime(new Date());
                dbConfig.setAddUser(loginUser.getUsername());
                newList.add(dbConfig);
            }
        }
        if(!newList.isEmpty()) {
            dbConfigMapper.batchInsert(newList);
        }

        return ResultUtils.success();
    }
}
