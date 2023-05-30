package com.tm.common.dao;

import com.tm.common.base.mapper.GlobalVariableMapper;
import com.tm.common.base.model.GlobalVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GlobalVariableDao {
    @Autowired
    private GlobalVariableMapper globalVariableMapper;

    public int updateBySelective(GlobalVariable record) {
        return globalVariableMapper.updateBySelective(record);
    }

    public GlobalVariable selectByPrimaryId(Integer id) {
        return globalVariableMapper.selectByPrimaryId(id);
    }
}
