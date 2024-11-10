package com.tm.common.dao;

import com.tm.common.base.mapper.AutoCaseMapper;
import com.tm.common.base.model.AutoCase;
import jakarta.inject.Inject;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class AutoCaseDao {
    private final AutoCaseMapper autoCaseMapper;

    @Inject
    public AutoCaseDao(AutoCaseMapper autoCaseMapper) {
        Assert.notNull(autoCaseMapper, "AutoCaseMapper must not be null!");
        this.autoCaseMapper = autoCaseMapper;
    }

    public AutoCase selectByPrimaryId(Integer id) {
        return autoCaseMapper.selectByPrimaryId(id);
    }
}
