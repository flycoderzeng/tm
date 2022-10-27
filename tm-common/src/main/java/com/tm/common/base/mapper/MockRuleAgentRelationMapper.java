package com.tm.common.base.mapper;

import com.tm.common.base.model.HttpMockRule;
import com.tm.common.base.model.MockRuleAgentRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MockRuleAgentRelationMapper {
    int insertBySelective(MockRuleAgentRelation record);
    int updateBySelective(MockRuleAgentRelation record);
    List<HttpMockRule> selectAllHttpMockRules(@Param("mockAgentId") Integer mockAgentId);
    int enable(Integer id);
    int disable(Integer id);
    int remove(Integer id);
}
