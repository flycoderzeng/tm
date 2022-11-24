package com.tm.common.base.mapper;

import com.tm.common.base.model.DbConfig;
import com.tm.common.entities.base.CommonTableQueryBody;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DbConfigMapper {
    DbConfig findByEnvIdAndDbName(@Param("envId") Integer envId,
                                  @Param("dbName") String dbName);
}
