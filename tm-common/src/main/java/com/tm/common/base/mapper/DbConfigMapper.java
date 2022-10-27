package com.tm.common.base.mapper;

import com.tm.common.base.model.DbConfig;
import com.tm.common.entities.base.CommonTableQueryBody;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DbConfigMapper {
    int deleteByPrimaryKey(int id);

    DbConfig findById(int id);

    List<DbConfig> queryList(CommonTableQueryBody body);

    int countList(CommonTableQueryBody body);

    int updateBySelective(DbConfig record);

    int insertBySelective(DbConfig record);

    DbConfig findByEnvIdAndDbName(@Param("envId") Integer envId,
                                  @Param("dbName") String dbName);
}
