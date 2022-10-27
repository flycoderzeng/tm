package com.tm.common.base.mapper;

import com.tm.common.base.model.HttpMockRule;
import com.tm.common.entities.base.CommonTableQueryBody;

import java.util.List;

public interface HttpMockRuleMapper {
    int deleteByPrimaryKey(int id);
    int insertBySelective(HttpMockRule record);
    int updateBySelective(HttpMockRule record);
    HttpMockRule selectByPrimaryId(Integer id);

    List<HttpMockRule> queryList(CommonTableQueryBody body);

    int countList(CommonTableQueryBody body);
}
