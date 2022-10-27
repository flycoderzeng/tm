package com.tm.web.controller.system;

import com.tm.web.controller.BaseController;
import com.tm.common.base.mapper.RightMapper;
import com.tm.common.base.model.Right;
import com.tm.common.base.model.User;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.CommonTableQueryBody;
import com.tm.common.entities.base.CommonTableQueryResponse;
import com.tm.common.entities.base.IdBody;
import com.tm.common.entities.common.enumerate.ResultCodeEnum;
import com.tm.common.utils.ResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping(value = "/right")
public class RightController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(ProjectController.class);
    @Autowired
    private RightMapper rightMapper;

    @PostMapping(value = "/queryRightList", produces = {"application/json;charset=UTF-8"})
    public BaseResponse queryRightList(@RequestBody @Valid CommonTableQueryBody body) {
        CommonTableQueryResponse response = new CommonTableQueryResponse<Right>();
        response.setRows(rightMapper.queryList(body));
        response.setTotal(rightMapper.countList(body));

        return ResultUtils.success(response);
    }


    @PostMapping(value = "/load" ,produces = {"application/json;charset=UTF-8"})
    public BaseResponse load(@RequestBody @Valid IdBody body) {
        Right right = rightMapper.findById(body.getId());
        return ResultUtils.success(right);
    }

    @PostMapping(value = "/save", produces = {"application/json;charset=UTF-8"})
    public BaseResponse save(@RequestBody Right body) {
        Right right;
        User loginUser = this.getLoginUser();
        if(body.getId() != null) {
            right = rightMapper.findById(body.getId());
            if(right == null) {
                return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
            }
        } else {
            right = new Right();
            right.setAddUser(loginUser.getUsername());
            right.setAddTime(new Date());
        }
        right.setName(body.getName());
        right.setUri(body.getUri());
        right.setType(body.getType());
        right.setLastModifyUser(loginUser.getUsername());
        right.setLastModifyTime(new Date());

        if(body.getId() == null) {
            rightMapper.insertBySelective(right);
        }else{
            rightMapper.updateBySelective(right);
        }
        return ResultUtils.success(right.getId());
    }

    @PostMapping(value = "/delete" ,produces = {"application/json;charset=UTF-8"})
    public BaseResponse delete(@RequestBody @Valid IdBody body) {
        return ResultUtils.success(rightMapper.deleteByPrimaryKey(body.getId()));
    }
}
