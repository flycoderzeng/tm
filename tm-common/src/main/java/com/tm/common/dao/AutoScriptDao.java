package com.tm.common.dao;

import com.tm.common.base.mapper.AutoScriptMapper;
import com.tm.common.base.model.AutoScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutoScriptDao {
    @Autowired
    private AutoScriptMapper autoScriptMapper;

    public AutoScript selectByPrimaryId(Integer id) {
        return autoScriptMapper.selectByPrimaryId(id);
    }
}
