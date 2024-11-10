package com.tm.common.dao;

import com.tm.common.base.mapper.GlobalVariableMapper;
import com.tm.common.base.model.GlobalVariable;
import jakarta.inject.Inject;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class GlobalVariableDao {
    private final GlobalVariableMapper globalVariableMapper;

    @Inject
    public GlobalVariableDao(GlobalVariableMapper globalVariableMapper) {
        Assert.notNull(globalVariableMapper, "GlobalVariableMapper must not be null!");
        this.globalVariableMapper = globalVariableMapper;
    }

    public int updateBySelective(GlobalVariable record) {
        return globalVariableMapper.updateBySelective(record);
    }

    public GlobalVariable selectByPrimaryId(Integer id) {
        return globalVariableMapper.selectByPrimaryId(id);
    }
}
