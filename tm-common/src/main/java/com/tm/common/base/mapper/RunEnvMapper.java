package com.tm.common.base.mapper;

import com.tm.common.base.model.RunEnv;
import com.tm.common.entities.base.CommonTableQueryBody;

import java.util.List;

public interface RunEnvMapper {
    List<RunEnv> queryList(CommonTableQueryBody body);
    int countList(CommonTableQueryBody body);
    RunEnv findById(Integer id);
    int updateBySelective(RunEnv record);
    int insertBySelective(RunEnv record);
    int deleteByPrimaryKey(int id);

    List<RunEnv> getAllRunEnv();
}
