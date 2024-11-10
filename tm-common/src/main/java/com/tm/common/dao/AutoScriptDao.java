package com.tm.common.dao;

import com.tm.common.base.mapper.AutoScriptMapper;
import com.tm.common.base.model.AutoScript;
import jakarta.inject.Inject;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class AutoScriptDao {
    private final AutoScriptMapper autoScriptMapper;

    @Inject
    public AutoScriptDao(AutoScriptMapper autoScriptMapper) {
        Assert.notNull(autoScriptMapper, "AutoScriptMapper must not be null!");
        this.autoScriptMapper = autoScriptMapper;
    }

    public AutoScript selectByPrimaryId(Integer id) {
        return autoScriptMapper.selectByPrimaryId(id);
    }
}
