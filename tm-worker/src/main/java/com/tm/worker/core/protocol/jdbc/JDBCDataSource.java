package com.tm.worker.core.protocol.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.tm.common.base.model.DbConfig;
import com.tm.common.entities.common.enumerate.DbTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class JDBCDataSource {
    private DruidDataSource dataSource;
    private DbConfig dbConfig;
    private Boolean isInit = false;
    public JDBCDataSource(DbConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public void init() {
        if(Boolean.TRUE.equals(isInit)) {
            return ;
        }
        dataSource = new DruidDataSource();
        dataSource.setUrl(getUrl());
        dataSource.setUsername(dbConfig.getUsername());
        dataSource.setPassword(dbConfig.getPassword());
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        if(DbTypeEnum.POSTGRESQL.value() == dbConfig.getType()) {
            dataSource.setDriverClassName("org.postgresql.Driver");
        }
        if(DbTypeEnum.DM.value() == dbConfig.getType()) {
            dataSource.setDriverClassName("dm.jdbc.driver.DmDriver");
        }
        dataSource.setInitialSize(3);
        dataSource.setMaxActive(30);
        dataSource.setMinIdle(1);
        // 配置获取连接等待超时的时间，单位毫秒。
        dataSource.setMaxWait(60000);
        dataSource.setValidationQuery("select 1");
        // 申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
        dataSource.setTestWhileIdle(true);
        // 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
        dataSource.setTestOnBorrow(false);
        // 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
        dataSource.setTestOnReturn(false);
        // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        // 配置一个连接在池中最小生存的时间，单位是毫秒
        dataSource.setMinEvictableIdleTimeMillis(300000);
        // 是否缓存preparedStatement，也就是PSCache。PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql下建议关闭
        dataSource.setPoolPreparedStatements(false);
        try {
            dataSource.init();
            isInit = true;
        } catch (SQLException exception) {
            log.error("初始话druid连接池失败, ", exception);
        }
    }

    public String getUrl() {
        String url = "jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&zeroDateTimeBehavior=convertToNull";
        url = String.format(url, dbConfig.getIp(), dbConfig.getPort(), dbConfig.getDbName());
        if(DbTypeEnum.POSTGRESQL.value() == dbConfig.getType()) {
            url = "jdbc:postgresql://%s:%s/%s?currentSchema=%s";
            url = String.format(url, dbConfig.getIp(), dbConfig.getPort(), dbConfig.getSchemaName(), dbConfig.getDbName());
        }
        if(DbTypeEnum.DM.value() == dbConfig.getType()) {
            url = "jdbc:dm://%s:%s/%s";
            url = String.format(url, dbConfig.getIp(), dbConfig.getPort(), dbConfig.getDbName());
        }
        log.info(url);
        return url;
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection(10000);
        } catch (SQLException sqlException) {
            log.error("获取数据库连接异常，", sqlException);
        }
        return null;
    }

    /**
     * 归还连接
     * @param connection
     */
    public void close(Connection connection) {
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException sqlException) {
                log.error("关闭数据库连接异常，", sqlException);
            }
        }
    }
}
