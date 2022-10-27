package com.tm.common.base.mapper;

import com.tm.common.base.model.Project;
import com.tm.common.entities.base.CommonTableQueryBody;

import java.util.List;

public interface ProjectMapper {
    List<Project> queryList(CommonTableQueryBody body);
    int countList(CommonTableQueryBody body);
    Project findById(Integer id);
    int updateBySelective(Project project);
    int insertBySelective(Project project);
    int deleteByPrimaryKey(int id);
    List<Project> getUserProjects(int id);
}
