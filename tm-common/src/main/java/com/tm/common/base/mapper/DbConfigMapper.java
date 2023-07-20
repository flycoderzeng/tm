package com.tm.common.base.mapper;

import com.tm.common.base.model.DbConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DbConfigMapper {
    DbConfig findByEnvIdDcnIdAndDbName(@Param("envId") Integer envId,
                                       @Param("dcnId") Integer dcnId,
                                       @Param("dbName") String dbName);

    List<DbConfig> findByEnvIdAndDcnId(@Param("envId") Integer envId,
                                       @Param("dcnId") Integer dcnId);

    List<DbConfig> findByEnvId(@Param("envId") Integer envId);

    List<DbConfig> getAllDatabaseNames();

    int batchInsert(@Param("dbConfigs") List<DbConfig> newList);

    DbConfig selectByPrimaryId(Integer id);

    int setDcnIdToNull(Integer id);
}
