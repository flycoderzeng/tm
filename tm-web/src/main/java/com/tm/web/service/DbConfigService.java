package com.tm.web.service;

import com.tm.common.base.mapper.DbConfigMapper;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("dbConfigService")
public class DbConfigService extends BaseService {
    @Autowired
    private DbConfigMapper dbConfigMapper;

    public BaseResponse getAllDatabaseNames() {
        return ResultUtils.success(dbConfigMapper.getAllDatabaseNames());
    }
}
