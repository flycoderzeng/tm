package com.tm.web.controller.system;

import com.tm.web.controller.BaseController;
import com.tm.common.base.mapper.UserMapper;
import com.tm.common.base.mapper.UserRoleMapper;
import com.tm.common.base.model.User;
import com.tm.common.base.model.UserRole;
import com.tm.common.base.model.UserRoleRelation;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.CommonTableQueryBody;
import com.tm.common.entities.base.CommonTableQueryResponse;
import com.tm.common.entities.base.IdBody;
import com.tm.common.entities.system.AddUserRoleBody;
import com.tm.common.entities.common.enumerate.ResultCodeEnum;
import com.tm.common.utils.ResultUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @PostMapping(value = "/queryUserList", produces = {"application/json;charset=UTF-8"})
    public BaseResponse queryUserList(@RequestBody @Valid CommonTableQueryBody body) {
        CommonTableQueryResponse response = new CommonTableQueryResponse<User>();
        response.setRows(userMapper.queryList(body));
        response.setTotal(userMapper.countList(body));

        return ResultUtils.success(response);
    }


    @PostMapping(value = "/findUser", produces = {"application/json;charset=UTF-8"})
    public BaseResponse findUser(@RequestBody @Valid CommonTableQueryBody body) {
        return ResultUtils.success(userMapper.queryList(body));
    }

    @PostMapping(value = "/load" ,produces = {"application/json;charset=UTF-8"})
    public BaseResponse load(@RequestBody @Valid IdBody body) {
        User user = userMapper.findById(body.getId());
        return ResultUtils.success(user);
    }

    @PostMapping(value = "/save", produces = {"application/json;charset=UTF-8"})
    public BaseResponse save(@RequestBody User body) {
        User user;
        User loginUser = this.getLoginUser();
        if(body.getId() != null) {
            user = userMapper.findById(body.getId());
            if(user == null) {
                return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
            }
        } else {
            user = new User();
            user.setAddUser(loginUser.getUsername());
            user.setAddTime(new Date());
        }
        user.setUsername(body.getUsername());
        user.setChineseName(body.getChineseName());
        user.setLastModifyUser(loginUser.getUsername());
        user.setLastModifyTime(new Date());
        if(StringUtils.isNotBlank(body.getPassword())) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
            user.setPassword(encoder.encode(body.getPassword()));
        }

        if(body.getId() == null) {
            userMapper.insertBySelective(user);
        }else{
            userMapper.updateBySelective(user);
        }
        return ResultUtils.success(user.getId());
    }

    @PostMapping(value = "/delete" ,produces = {"application/json;charset=UTF-8"})
    public BaseResponse delete(@RequestBody @Valid IdBody body) {
        return ResultUtils.success(userMapper.deleteByPrimaryKey(body.getId()));
    }

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
