package com.tm.common.dao;

import com.tm.common.base.mapper.AutoCaseMapper;
import com.tm.common.base.model.AutoCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutoCaseDao {
    @Autowired
    private AutoCaseMapper autoCaseMapper;

    public AutoCase selectByPrimaryId(Integer id) {
        return autoCaseMapper.selectByPrimaryId(id);
    }
}
