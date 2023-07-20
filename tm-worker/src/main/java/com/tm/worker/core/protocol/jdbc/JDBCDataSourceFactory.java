package com.tm.worker.core.protocol.jdbc;

import com.tm.common.base.model.DbConfig;
import com.tm.common.utils.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class JDBCDataSourceFactory {
    @Value("${rsa.privateKey}")
    private String privateKeyStr;

    public final Map<String, JDBCDataSource> dataSourceMap = new ConcurrentHashMap<>();

    /**
     * 归还连接
     * @param dbConfig
     * @param connection
     */
    public void closeConnection(DbConfig dbConfig, Connection connection) {
        String key = getDataSourceKey(dbConfig);
        if(dataSourceMap.containsKey(key)) {
            dataSourceMap.get(key).close(connection);
        }
    }

    public Connection getConnection(DbConfig dbConfig) {
        String key = getDataSourceKey(dbConfig);
        if(!dataSourceMap.containsKey(key)) {
            JDBCDataSource jdbcDataSource = initDataSource(dbConfig);
            dataSourceMap.put(key, jdbcDataSource);
        }
        if(dataSourceMap.containsKey(key)) {
            return dataSourceMap.get(key).getConnection();
        }
        return null;
    }

    public String getDataSourceKey(DbConfig dbConfig) {
        int dcnId = dbConfig.getDcnId() == null? 0 : dbConfig.getDcnId();

        return dbConfig.getDbName() + "-" + dbConfig.getEnvId() + "-" + dcnId;
    }

    private synchronized JDBCDataSource initDataSource(DbConfig dbConfig) {
        String key = getDataSourceKey(dbConfig);
        if(dataSourceMap.containsKey(key)) {
            return dataSourceMap.get(key);
        }
        dbConfig.setUsername(RSAUtils.decrypt(privateKeyStr, dbConfig.getUsername()));
        dbConfig.setPassword(RSAUtils.decrypt(privateKeyStr, dbConfig.getPassword()));

        JDBCDataSource jdbcDataSource = new JDBCDataSource(dbConfig);
        jdbcDataSource.init();
        return jdbcDataSource;
    }
}
