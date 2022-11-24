package com.tm.common.base.mapper;

import com.tm.common.base.model.Project;
import com.tm.common.entities.base.CommonTableQueryBody;

import java.util.List;

public interface ProjectMapper {
    List<Project> getUserProjects(int id);
}
