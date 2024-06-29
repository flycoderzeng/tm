package com.tm.ma.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


public class BaseDataSource {
    public DataSource dataSource;
    Logger logger = LoggerFactory.getLogger(BaseDataSource.class);

    public Connection getConnection() {
        if (dataSource == null) {
            return null;
        }
        try {
            long start = System.currentTimeMillis();
            Connection connection = dataSource.getConnection();
            long end = System.currentTimeMillis();
            logger.info("获取连接耗时: {}ms", end - start);
            if(dataSource instanceof DruidDataSource) {
                logger.info("当前活动连接数: {}, 当前连接数: {}", ((DruidDataSource)dataSource).getActiveCount(),
                        ((DruidDataSource)dataSource).getPoolingCount());
            }
            if(dataSource instanceof PoolingDataSource) {
                logger.info("当前dataSource 类型是: PoolingDataSource");
            }
            return connection;
        } catch (SQLException sqlException) {
            logger.error("获取数据库连接异常，", sqlException);
        }
        return null;
    }

    public void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException sqlException) {
                logger.error("关闭数据库连接异常，", sqlException);
            }
        }
    }

    public void close() {
        if(dataSource instanceof DruidDataSource) {
            ((DruidDataSource)dataSource).close();
        }else if(dataSource instanceof HikariDataSource) {
            ((HikariDataSource)dataSource).close();
        }
    }
}

