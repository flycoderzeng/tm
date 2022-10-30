package com.tm.web.controller.system;

import com.tm.web.controller.BaseController;
import com.tm.common.base.mapper.ProjectMapper;
import com.tm.common.base.mapper.ProjectUserMapper;
import com.tm.common.base.mapper.ProjectUserRoleMapper;
import com.tm.common.base.model.*;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.CommonTableQueryResponse;
import com.tm.common.entities.base.IdBody;
import com.tm.common.entities.system.AddUserToProjectBody;
import com.tm.common.entities.system.DeleteProjectUserRoleBody;
import com.tm.common.entities.system.QueryProjectUserBody;
import com.tm.common.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/project")
public class ProjectController extends BaseController {
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ProjectUserMapper projectUserMapper;
    @Autowired
    private ProjectUserRoleMapper projectUserRoleMapper;

    @PostMapping(value = "/addProjectUser", produces = {"application/json;charset=UTF-8"})
    public BaseResponse addProjectUser(@RequestBody @Valid AddUserToProjectBody body) {
        User loginUser = this.getLoginUser();
        for (Integer userId : body.getUserIdList()) {
            ProjectUser projectUser = new ProjectUser();
            projectUser.setProjectId(body.getProjectId());
            projectUser.setUserId(userId);
            ProjectUser projectUser1 = projectUserMapper.selectByProjectUser(projectUser);
            if(projectUser1 == null) {
                projectUser.setAddUser(loginUser.getUsername());
                projectUser.setAddTime(new Date());
                projectUserMapper.insertBySelective(projectUser);
            }else{
                projectUser.setLastModifyUser(loginUser.getUsername());
                projectUser.setLastModifyTime(new Date());
                projectUser.setId(projectUser1.getId());
                projectUserMapper.updateBySelective(projectUser);
            }

            for (Integer roleId : body.getRoleIdList()) {
                ProjectUserRole projectUserRole = new ProjectUserRole();
                projectUserRole.setProjectId(body.getProjectId());
                projectUserRole.setUserId(userId);
                projectUserRole.setRoleId(roleId);
                ProjectUserRole projectUserRole1 = projectUserRoleMapper.selectByProjectUserRole(projectUserRole);
                if(projectUserRole1 == null) {
                    projectUserRole.setAddUser(loginUser.getUsername());
                    projectUserRole.setAddTime(new Date());
                    projectUserRoleMapper.insertBySelective(projectUserRole);
                }else{
                    projectUserRole.setLastModifyUser(loginUser.getUsername());
                    projectUserRole.setLastModifyTime(new Date());
                    projectUserRole.setId(projectUserRole1.getId());
                    projectUserRoleMapper.updateBySelective(projectUserRole);
                }
            }
        }
        return ResultUtils.success();
    }

    @PostMapping(value = "/deleteProjectUser", produces = {"application/json;charset=UTF-8"})
    public BaseResponse deleteProjectUser(@RequestBody @Valid IdBody body) {
        ProjectUser projectUser = projectUserMapper.selectByPrimaryKey(body.getId());
        if(projectUser == null) {
            return ResultUtils.success();
        }
        projectUserMapper.deleteByPrimaryKey(body.getId());
        projectUserRoleMapper.deleteByProjectIdUserId(projectUser);
        return ResultUtils.success();
    }

    @PostMapping(value = "/deleteProjectUserRole", produces = {"application/json;charset=UTF-8"})
    public BaseResponse deleteProjectUserRole(@RequestBody @Valid DeleteProjectUserRoleBody body) {
        projectUserRoleMapper.deleteProjectUserRole(body);
        return ResultUtils.success();
    }

    @PostMapping(value = "/queryProjectUserList", produces = {"application/json;charset=UTF-8"})
    public BaseResponse queryProjectUserList(@RequestBody @Valid QueryProjectUserBody body) {
        CommonTableQueryResponse response = new CommonTableQueryResponse<ProjectUserRelation>();
        List<ProjectUserRelation> list = projectUserMapper.queryProjectUserList(body);
        for (ProjectUserRelation projectUserRelation : list) {
            List<ProjectUserRole> roleList = projectUserRoleMapper.getUserRoleList(projectUserRelation);
            projectUserRelation.setRoleList(roleList);
        }
        response.setRows(list);
        int total = projectUserMapper.countProjectUserList(body);
        response.setTotal(total);

        return ResultUtils.success(response);
    }

    @GetMapping(value = "/getMyProjects")
    public BaseResponse getMyProjects() {
        User u = this.getLoginUser();
        List<Project> list = projectMapper.getUserProjects(u.getId());
        return ResultUtils.success(list);
    }
}
