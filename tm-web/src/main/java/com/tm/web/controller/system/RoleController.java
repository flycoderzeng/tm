package com.tm.web.controller.system;

import com.tm.web.controller.BaseController;
import com.tm.common.base.mapper.RoleMapper;
import com.tm.common.base.mapper.RoleRightMapper;
import com.tm.common.base.model.*;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.CommonTableQueryBody;
import com.tm.common.entities.base.CommonTableQueryResponse;
import com.tm.common.entities.base.IdBody;
import com.tm.common.entities.system.AddRoleRightBody;
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
import java.util.List;

@RestController
@RequestMapping(value = "/role")
public class RoleController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(ProjectController.class);
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleRightMapper roleRightMapper;

    @PostMapping(value = "/queryRoleList", produces = {"application/json;charset=UTF-8"})
    public BaseResponse queryRoleList(@RequestBody @Valid CommonTableQueryBody body) {
        CommonTableQueryResponse response = new CommonTableQueryResponse<Role>();
        response.setRows(roleMapper.queryList(body));
        response.setTotal(roleMapper.countList(body));

        return ResultUtils.success(response);
    }

    @PostMapping(value = "/queryRoleRightList", produces = {"application/json;charset=UTF-8"})
    public BaseResponse queryRoleRightList(@RequestBody @Valid IdBody body) {
        List<RoleRightRelation> list = roleRightMapper.queryRoleRightList(body.getId());

        return ResultUtils.success(list);
    }

    @PostMapping(value = "/deleteRoleRight", produces = {"application/json;charset=UTF-8"})
    public BaseResponse deleteRoleRight(@RequestBody @Valid IdBody body) {
        roleRightMapper.deleteByPrimaryKey(body.getId());
        return ResultUtils.success();
    }

    @PostMapping(value = "/addRoleRight", produces = {"application/json;charset=UTF-8"})
    public BaseResponse addRoleRight(@RequestBody @Valid AddRoleRightBody body) {
        User loginUser = this.getLoginUser();
        body.getRightIdList().forEach(rightId->{
            RoleRight roleRight = new RoleRight();
            roleRight.setRoleId(body.getRoleId());
            roleRight.setRightId(rightId);
            if(roleRightMapper.selectByRoleIdRightId(roleRight) == null) {
                roleRight.setAddUser(loginUser.getUsername());
                roleRight.setAddTime(new Date());
                roleRight.setLastModifyUser(loginUser.getUsername());
                roleRight.setLastModifyTime(new Date());
                roleRightMapper.insertBySelective(roleRight);
            }
        });
        return ResultUtils.success();
    }

    @PostMapping(value = "/load" ,produces = {"application/json;charset=UTF-8"})
    public BaseResponse load(@RequestBody @Valid IdBody body) {
        Role role = roleMapper.findById(body.getId());
        return ResultUtils.success(role);
    }

    @PostMapping(value = "/save", produces = {"application/json;charset=UTF-8"})
    public BaseResponse save(@RequestBody Role body) {
        Role role;
        User loginUser = this.getLoginUser();
        if(body.getId() != null) {
            role = roleMapper.findById(body.getId());
            if(role == null) {
                return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
            }
        } else {
            role = new Role();
            role.setAddUser(loginUser.getUsername());
            role.setAddTime(new Date());
        }
        role.setName(body.getName());
        role.setDescription(body.getDescription());
        role.setChineseName(body.getChineseName());
        role.setType(body.getType());
        role.setLastModifyUser(loginUser.getUsername());
        role.setLastModifyTime(new Date());

        if(body.getId() == null) {
            roleMapper.insertBySelective(role);
        }else{
            roleMapper.updateBySelective(role);
        }
        return ResultUtils.success(role.getId());
    }

    @PostMapping(value = "/delete" ,produces = {"application/json;charset=UTF-8"})
    public BaseResponse delete(@RequestBody @Valid IdBody body) {
        return ResultUtils.success(roleMapper.deleteByPrimaryKey(body.getId()));
    }
}
