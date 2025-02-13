package com.tm.web.service;

import com.tm.common.base.mapper.DataNodeMapper;
import com.tm.common.base.mapper.GlobalVariableMapper;
import com.tm.common.base.model.DataNode;
import com.tm.common.base.model.GlobalVariable;
import com.tm.common.base.model.User;
import com.tm.common.entities.autotest.request.SaveNodeBody;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.common.enumerate.DataTypeEnum;
import com.tm.common.utils.ResultUtils;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("globalVariableService")
public class GlobalVariableService extends BaseService {
    private final GlobalVariableMapper globalVariableMapper;
    private final DataNodeMapper dataNodeMapper;

    @Inject
    public GlobalVariableService(GlobalVariableMapper globalVariableMapper, DataNodeMapper dataNodeMapper) {
        this.globalVariableMapper = globalVariableMapper;
        this.dataNodeMapper = dataNodeMapper;
    }

    public BaseResponse copy(SaveNodeBody body) {
        return ResultUtils.success();
    }

    public GlobalVariable load(Integer id) {
        GlobalVariable globalVariable = globalVariableMapper.selectByPrimaryId(id);
        DataNode dataNode = dataNodeMapper.selectByPrimaryKey(id, DataTypeEnum.GLOBAL_VARIABLE.value());
        setDataNodeInfo(globalVariable, dataNode);
        return globalVariable;
    }

    public BaseResponse update(GlobalVariable globalVariable, User user) {
        globalVariableMapper.updateBySelective(globalVariable);
        return updateNode4CommonFields(user, globalVariable, DataTypeEnum.GLOBAL_VARIABLE);
    }
}
