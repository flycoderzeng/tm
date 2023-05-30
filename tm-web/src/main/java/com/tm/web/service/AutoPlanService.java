package com.tm.web.service;

import com.tm.common.base.mapper.AutoPlanMapper;
import com.tm.common.base.model.AutoPlan;
import com.tm.common.base.model.User;
import com.tm.common.entities.autotest.request.SaveNodeBody;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.common.enumerate.DataTypeEnum;
import com.tm.common.entities.common.enumerate.ResultCodeEnum;
import com.tm.common.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("autoPlanService")
public class AutoPlanService extends BaseService {

    @Autowired
    private AutoPlanMapper autoPlanMapper;

    public BaseResponse copy(SaveNodeBody body) {
        return ResultUtils.success();
    }

    public BaseResponse load(Integer id) {
        AutoPlan autoPlan = autoPlanMapper.selectByPrimaryId(id);
        return ResultUtils.success(autoPlan);
    }

    public BaseResponse save(AutoPlan autoPlan, User user) {
        if(autoPlan.getId() != null || autoPlan.getId() > 0) {
            autoPlanMapper.updateBySelective(autoPlan);
        }else{
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
        return updateNode4CommonFields(user, autoPlan, DataTypeEnum.AUTO_PLAN);
    }

    public BaseResponse savePlanVariables(AutoPlan autoPlan, User user) {
        if(autoPlan.getId() != null || autoPlan.getId() > 0) {
            autoPlanMapper.updateBySelective(autoPlan);
            return ResultUtils.success();
        }else{
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
    }
}
