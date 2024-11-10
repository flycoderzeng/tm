package com.tm.worker.service;

import com.tm.common.base.mapper.DbConfigMapper;
import com.tm.common.base.model.DbConfig;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service(value = "dbConfigService")
public class DbConfigService {
    private final DbConfigMapper dbConfigMapper;

    @Inject
    public DbConfigService(DbConfigMapper dbConfigMapper) {
        this.dbConfigMapper = dbConfigMapper;
    }

    public DbConfig findDbConfig(Integer envId, Integer dcnId, String dbName) {
        return dbConfigMapper.findByEnvIdDcnIdAndDbName(envId, dcnId, dbName);
    }
}
