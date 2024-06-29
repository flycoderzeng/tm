package com.tm.ma.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONObject;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp2.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class JdbcDataSource extends BaseDataSource {
    public JDBCConfig jdbcConfig;
    public Boolean isInit = false;
    Logger logger = LoggerFactory.getLogger(JdbcDataSource.class);

    public JdbcDataSource(JDBCConfig jdbcConfig) {
        this.jdbcConfig = jdbcConfig;
    }

    public void initDataSource() {
        logger.info("jdbcConfig===> {}", JSONObject.toJSONString(jdbcConfig));
        if (StringUtils.equals(jdbcConfig.getPoolType(), "druid")) {
            logger.info("初始化为 DruidDataSource");
            initDruidDataSource();
        } else if (StringUtils.equals(jdbcConfig.getPoolType(), "HikariCP")) {
            logger.info("初始化为 HikariDataSource");
            initHikariCPDataSource();
        } else if (StringUtils.equals(jdbcConfig.getPoolType(), "ApacheDBCP")) {
            logger.info("初始化为 ApacheDBCPDataSource");
            initApacheDBCPBasicDataSource();
        }
    }

    public void initApacheDBCPBasicDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(jdbcConfig.getDriverClassName());
        ds.setUrl(jdbcConfig.getJdbcUrl());
        ds.setUsername(jdbcConfig.getUsername());
        ds.setPassword(jdbcConfig.getPassword());
        ds.setMaxWaitMillis(60000);
        ds.setMaxConnLifetimeMillis(60000);
        ds.setInitialSize(jdbcConfig.getMinPoolSize());
        ds.setMaxTotal(jdbcConfig.getMaxPoolSize());
        if (jdbcConfig.getJdbcUrl().contains("oracle")) {
            ds.setValidationQuery("select 1 from dual");
        }else {
            ds.setValidationQuery("select 1");
        }
        dataSource = ds;
    }

    public void initApacheDBCPPoolDataSource() {
        try {
            Class.forName(jdbcConfig.getDriverClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ConnectionFactory connectionFactory =
                new DriverManagerConnectionFactory(jdbcConfig.getJdbcUrl(), jdbcConfig.getUsername(), jdbcConfig.getPassword());
        PoolableConnectionFactory poolableConnectionFactory =
                new PoolableConnectionFactory(connectionFactory, null);
        ObjectPool<PoolableConnection> connectionPool =
                new GenericObjectPool<PoolableConnection>(poolableConnectionFactory);

        poolableConnectionFactory.setPool(connectionPool);
        dataSource = new PoolingDataSource<PoolableConnection>(connectionPool);
    }

    public void initHikariCPDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(jdbcConfig.getDriverClassName());
        config.setJdbcUrl(jdbcConfig.getJdbcUrl());
        config.setUsername(jdbcConfig.getUsername());
        config.setPassword(jdbcConfig.getPassword());
        config.setConnectionTimeout(60000);
        config.setMaximumPoolSize(jdbcConfig.getMaxPoolSize());
        config.setMinimumIdle(jdbcConfig.getMinPoolSize());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("autoReconnect", "true");
        dataSource = new HikariDataSource(config);
    }

    public void initDruidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(jdbcConfig.getJdbcUrl());
        druidDataSource.setUsername(jdbcConfig.getUsername());
        druidDataSource.setPassword(jdbcConfig.getPassword());
        druidDataSource.setDriverClassName(jdbcConfig.getDriverClassName());
        druidDataSource.setInitialSize(jdbcConfig.getMinPoolSize());
        druidDataSource.setMaxActive(jdbcConfig.getMaxPoolSize());
        druidDataSource.setMinIdle(60);
        // 配置获取连接等待超时的时间，单位毫秒。
        druidDataSource.setMaxWait(60000);
        if (jdbcConfig.getJdbcUrl().contains("oracle")) {
            druidDataSource.setValidationQuery("select 1 from dual");
        } else {
            druidDataSource.setValidationQuery("select 1");
        }
        // 申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
        druidDataSource.setTestWhileIdle(true);
        // 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
        druidDataSource.setTestOnBorrow(false);
        // 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
        druidDataSource.setTestOnReturn(false);
        // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        druidDataSource.setTimeBetweenEvictionRunsMillis(500);
        // 配置一个连接在池中最小生存的时间，单位是毫秒
        druidDataSource.setMinEvictableIdleTimeMillis(300000);
        // 是否缓存preparedStatement，也就是PSCache。PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql下建议关闭
        druidDataSource.setPoolPreparedStatements(false);
        druidDataSource.setDefaultAutoCommit(true);
        druidDataSource.setConnectionErrorRetryAttempts(10);
        druidDataSource.setTimeBetweenConnectErrorMillis(1000);
        try {
            druidDataSource.init();
        } catch (SQLException exception) {
            logger.error("初始话druid连接池失败, ", exception);
        }
        logger.info("当前活动连接数: {}, 当前连接数: {}", druidDataSource.getActiveCount(), druidDataSource.getConnectCount());
        dataSource = druidDataSource;
    }

    @Override
    public Connection getConnection() {
        if (Boolean.FALSE.equals(isInit)) {
            initDataSource();
            isInit = true;
        }
        return super.getConnection();
    }
}

