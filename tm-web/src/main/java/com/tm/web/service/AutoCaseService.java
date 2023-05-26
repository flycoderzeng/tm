package com.tm.web.service;


import com.tm.common.base.mapper.AutoCaseHistoryMapper;
import com.tm.common.base.mapper.AutoCaseMapper;
import com.tm.common.base.model.AutoCase;
import com.tm.common.base.model.AutoCaseHistory;
import com.tm.common.base.model.User;
import com.tm.common.entities.autotest.request.SaveNodeBody;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.common.enumerate.DataTypeEnum;
import com.tm.common.entities.common.enumerate.ResultCodeEnum;
import com.tm.common.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service("autoTestService")
public class AutoCaseService extends BaseService {
    @Autowired
    private AutoCaseMapper autoCaseMapper;

    @Autowired
    private AutoCaseHistoryMapper autoCaseHistoryMapper;


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
            final AutoCase oldCase = autoCaseMapper.selectByPrimaryId(autoCase.getId());
            if(!StringUtils.equals(autoCase.getSteps(), oldCase.getSteps()) ||
                    !StringUtils.equals(autoCase.getGroupVariables(), oldCase.getGroupVariables())) {
                saveCaseHistory(autoCase, user, oldCase);
                autoCaseMapper.updateBySelective(autoCase);
                return updateNode4CommonFields(user, autoCase, DataTypeEnum.AUTO_CASE);
            }
        }else{
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
        return ResultUtils.success();
    }

    private void saveCaseHistory(AutoCase autoCase, User user, AutoCase oldCase) {
        AutoCaseHistory history = new AutoCaseHistory();
        history.setAutoCaseId(autoCase.getId());
        history.setSteps(oldCase.getSteps());
        history.setGroupVariables(oldCase.getGroupVariables());
        history.setAddUser(user.getUsername());
        history.setAddTime(new Date());
        autoCaseHistoryMapper.insertBySelective(history);
    }

    public BaseResponse loadAutoCase(Integer id) {
        return ResultUtils.success(autoCaseMapper.selectByPrimaryId(id));
    }
}
