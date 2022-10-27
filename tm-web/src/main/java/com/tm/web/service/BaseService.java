package com.tm.web.service;

import com.tm.common.entities.base.CommonTableQueryBody;
import com.tm.common.entities.common.enumerate.DataTypeEnum;
import com.tm.common.base.mapper.DataNodeMapper;
import com.tm.common.base.model.*;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.common.enumerate.ResultCodeEnum;
import com.tm.common.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service("baseService")
public class BaseService {
    @Autowired
    private DataNodeMapper dataNodeMapper;

    public BaseResponse updateNode4CommonFields(User user, Object object, DataTypeEnum resourceType) {
        DataNode dataNode = null;
        switch (resourceType) {
            case AUTO_SHELL:
                AutoScript autoScript = (AutoScript)object;
                dataNode = dataNodeMapper.selectByPrimaryKey(autoScript.getId(), DataTypeEnum.AUTO_SHELL.value());
                break;
            case APP_API:
                HttpApi httpApi = (HttpApi)object;
                dataNode = dataNodeMapper.selectByPrimaryKey(httpApi.getId(), DataTypeEnum.APP_API.value());
                break;
            case AUTO_CASE:
                AutoCase autoCase = (AutoCase) object;
                dataNode = dataNodeMapper.selectByPrimaryKey(autoCase.getId(), DataTypeEnum.AUTO_CASE.value());
                break;
            case GLOBAL_VARIABLE:
                GlobalVariable globalVariable = (GlobalVariable)object;
                dataNode = dataNodeMapper.selectByPrimaryKey(globalVariable.getId(), DataTypeEnum.GLOBAL_VARIABLE.value());
                break;
            case PLATFORM_API:
                PlatformApi platformApi = (PlatformApi) object;
                dataNode = dataNodeMapper.selectByPrimaryKey(platformApi.getId(), DataTypeEnum.PLATFORM_API.value());
                break;
            case AUTO_PLAN:
                AutoPlan autoPlan = (AutoPlan) object;
                dataNode = dataNodeMapper.selectByPrimaryKey(autoPlan.getId(), DataTypeEnum.AUTO_PLAN.value());
                break;
            default:
                break;
        }
        if(dataNode == null) {
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
        dataNode.setLastModifyUserId(user.getId());
        dataNode.setLastModifyTime(new Date());
        if(!resourceType.equals(DataTypeEnum.AUTO_CASE)) {
            CommonNameDescriptionModel commonNameDescriptionModel = (CommonNameDescriptionModel)object;
            dataNode.setName(commonNameDescriptionModel.getName());
            dataNode.setDescription(commonNameDescriptionModel.getDescription());
        }

        dataNodeMapper.updateBySelective(dataNode);
        return ResultUtils.success(dataNode.getId());
    }

    public void setQueryUserInfo(CommonTableQueryBody body, User loginUser) {
        body.setLoginUserId(loginUser.getId());
        body.setLoginUsername(loginUser.getUsername());
    }
}
