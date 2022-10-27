package com.tm.common.base.mapper;

import com.tm.common.base.model.ProjectUser;
import com.tm.common.base.model.ProjectUserRole;
import com.tm.common.entities.system.DeleteProjectUserRoleBody;

import java.util.List;

public interface ProjectUserRoleMapper {
    int deleteByProjectIdUserId(ProjectUser projectUser);
    int deleteProjectUserRole(DeleteProjectUserRoleBody body);
    int insertBySelective(ProjectUserRole projectUserRole);
    int updateBySelective(ProjectUserRole projectUserRole);
    ProjectUserRole selectByProjectUserRole(ProjectUserRole projectUserRole);
    List<ProjectUserRole> getUserRoleList(ProjectUser projectUser);
}
