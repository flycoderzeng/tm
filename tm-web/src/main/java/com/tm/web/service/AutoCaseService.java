package com.tm.web.service;


import com.tm.common.base.mapper.AutoCaseMapper;
import com.tm.common.base.model.AutoCase;
import com.tm.common.base.model.User;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.autotest.request.SaveNodeBody;
import com.tm.common.entities.common.enumerate.DataTypeEnum;
import com.tm.common.entities.common.enumerate.ResultCodeEnum;
import com.tm.common.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("autoTestService")
public class AutoCaseService extends BaseService {
    @Autowired
    private AutoCaseMapper autoCaseMapper;


    public BaseResponse copy(SaveNodeBody body) {
        AutoCase autoCase = autoCaseMapper.selectByPrimaryId(body.getCopyId());
        if(autoCase == null) {
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
        autoCase.setId(body.getId());
        autoCaseMapper.updateBySelective(autoCase);
        return ResultUtils.success();
    }

    public BaseResponse saveAutoCase(AutoCase autoCase, User user) {
        if(autoCase.getId() != null || autoCase.getId() > 0) {
            autoCaseMapper.updateBySelective(autoCase);
        }else{
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
        return updateNode4CommonFields(user, autoCase, DataTypeEnum.AUTO_CASE);
    }

    public BaseResponse loadAutoCase(Integer id) {
        return ResultUtils.success(autoCaseMapper.selectByPrimaryId(id));
    }
}
