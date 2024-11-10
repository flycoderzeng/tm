package com.tm.web.controller.system;

import com.tm.common.base.mapper.RoleRightMapper;
import com.tm.common.base.model.RoleRight;
import com.tm.common.base.model.User;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.system.AddRoleRightBody;
import com.tm.common.utils.ResultUtils;
import com.tm.web.controller.BaseController;
import jakarta.inject.Inject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping(value = "/role")
public class RoleController extends BaseController {
    private final RoleRightMapper roleRightMapper;

    @Inject
    public RoleController(RoleRightMapper roleRightMapper) {
        this.roleRightMapper = roleRightMapper;
    }

    @PostMapping(value = "/deleteRoleRight", produces = {"application/json;charset=UTF-8"})
    public BaseResponse deleteRoleRight(@RequestBody @Valid RoleRight body) {
        final RoleRight roleRight = roleRightMapper.selectByRoleIdRightId(body);
        if(roleRight != null) {
            roleRightMapper.deleteByPrimaryKey(roleRight.getId());
        }
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
}
