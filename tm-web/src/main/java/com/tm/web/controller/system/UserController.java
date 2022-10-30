package com.tm.web.controller.system;

import com.tm.web.controller.BaseController;
import com.tm.common.base.mapper.UserRoleMapper;
import com.tm.common.base.model.User;
import com.tm.common.base.model.UserRole;
import com.tm.common.base.model.UserRoleRelation;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.IdBody;
import com.tm.common.entities.system.AddUserRoleBody;
import com.tm.common.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController extends BaseController {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @PostMapping(value = "/queryUserRoleList", produces = {"application/json;charset=UTF-8"})
    public BaseResponse queryUserRoleList(@RequestBody @Valid IdBody body) {
        List<UserRoleRelation> list = userRoleMapper.queryUserRoleList(body.getId());

        return ResultUtils.success(list);
    }

    @PostMapping(value = "/deleteUserRole", produces = {"application/json;charset=UTF-8"})
    public BaseResponse deleteUserRole(@RequestBody @Valid IdBody body) {
        userRoleMapper.deleteByPrimaryKey(body.getId());
        return ResultUtils.success();
    }

    @PostMapping(value = "/addUserRole", produces = {"application/json;charset=UTF-8"})
    public BaseResponse addRoleRight(@RequestBody @Valid AddUserRoleBody body) {
        User loginUser = this.getLoginUser();
        body.getRoleIdList().forEach(roleId->{
            UserRole userRole = new UserRole();
            userRole.setUserId(body.getUserId());
            userRole.setRoleId(roleId);
            if(userRoleMapper.selectByUserIdRoleId(userRole) == null) {
                userRole.setAddUser(loginUser.getUsername());
                userRole.setAddTime(new Date());
                userRole.setLastModifyUser(loginUser.getUsername());
                userRole.setLastModifyTime(new Date());
                userRoleMapper.insertBySelective(userRole);
            }
        });
        return ResultUtils.success();
    }
}
