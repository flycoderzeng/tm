package com.tm.web.service;

import com.tm.common.base.mapper.ProjectMapper;
import com.tm.common.entities.testmanage.ProjectStatisticsInfo;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("projectService")
public class ProjectService extends BaseService {
    private final ProjectMapper projectMapper;

    @Inject
    public ProjectService(ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
    }

    public ProjectStatisticsInfo getProjectReportInfo(Integer projectId) {
        ProjectStatisticsInfo projectStatisticsInfo = new ProjectStatisticsInfo();
        projectStatisticsInfo.setTotalCase(projectMapper.getCountCase(projectId));
        projectStatisticsInfo.setTotalPlan(projectMapper.getCountPlan(projectId));

        return projectStatisticsInfo;
    }
}
