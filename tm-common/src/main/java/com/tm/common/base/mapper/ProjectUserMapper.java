package com.tm.common.base.mapper;

import com.tm.common.base.model.ProjectUser;
import com.tm.common.base.model.ProjectUserRelation;
import com.tm.common.entities.system.QueryProjectUserBody;

import java.util.List;

public interface ProjectUserMapper {
    List<ProjectUserRelation> queryProjectUserList(QueryProjectUserBody body);
    int countProjectUserList(QueryProjectUserBody body);
    int deleteByPrimaryKey(Integer id);
    ProjectUser selectByPrimaryKey(Integer id);
    ProjectUser selectByProjectUser(ProjectUser projectUser);
    int insertBySelective(ProjectUser projectUser);
    int updateBySelective(ProjectUser projectUser);
}
