package com.tm.worker.service;

import com.tm.common.base.mapper.DbConfigMapper;
import com.tm.common.base.model.DbConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service(value = "dbConfigService")
public class DbConfigService {
    @Autowired
    private DbConfigMapper dbConfigMapper;

    public DbConfig findDbConfig(Integer envId, Integer dcnId, String dbName) {
        return dbConfigMapper.findByEnvIdDcnIdAndDbName(envId, dcnId, dbName);
    }
}
