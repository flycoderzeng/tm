package com.tm.common.base.mapper;

import com.tm.common.base.model.MockAgentInstance;
import com.tm.common.entities.base.CommonTableQueryBody;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MockAgentInstanceMapper {
    int insertBySelective(MockAgentInstance record);
    int updateBySelective(MockAgentInstance record);
    int lightenAgent(Integer id);
    int setAgentToOffline(Integer id);
    int setAllAgentToOffline();
    MockAgentInstance selectByIpAndPort(@Param("ip") String ip,
                                        @Param("port") Integer port);
    MockAgentInstance selectByPrimaryKey(Integer id);
    int countList(CommonTableQueryBody body);
    List<MockAgentInstance> queryList(CommonTableQueryBody body);
}
