package com.tm.common.base.mapper;

import com.tm.common.base.model.Project;

import java.util.List;

public interface ProjectMapper {
    List<Project> getUserProjects(int id);
    int getCountCase(int id);
    int getCountPlan(int id);
}
