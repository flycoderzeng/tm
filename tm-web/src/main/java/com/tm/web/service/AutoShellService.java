package com.tm.web.service;

import com.tm.common.base.mapper.AutoScriptMapper;
import com.tm.common.base.mapper.DataDictMapper;
import com.tm.common.base.model.AutoScript;
import com.tm.common.base.model.DataDictModel;
import com.tm.common.base.model.User;
import com.tm.common.entities.autotest.request.SaveNodeBody;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.common.enumerate.DataTypeEnum;
import com.tm.common.utils.ResultUtils;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("autoShellService")
public class AutoShellService extends BaseService {
    private final DataDictMapper dataDictMapper;
    private final AutoScriptMapper autoScriptMapper;

    @Inject
    public AutoShellService(DataDictMapper dataDictMapper, AutoScriptMapper autoScriptMapper) {
        this.dataDictMapper = dataDictMapper;
        this.autoScriptMapper = autoScriptMapper;
    }

    public BaseResponse copy(SaveNodeBody body) {
        return ResultUtils.success();
    }

    public List<DataDictModel> getScriptTypeList() {
        return dataDictMapper.selectAll(10000);
    }

    public AutoScript load(Integer id) {
        return autoScriptMapper.selectByPrimaryId(id);
    }

    public BaseResponse update(AutoScript autoScript, User user) {
        autoScriptMapper.updateBySelective(autoScript);
        return updateNode4CommonFields(user, autoScript, DataTypeEnum.AUTO_SHELL);
    }
}
