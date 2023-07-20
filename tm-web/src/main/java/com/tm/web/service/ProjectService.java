package com.tm.web.service;

import com.tm.common.base.mapper.ProjectMapper;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.testmanage.ProjectStatisticsInfo;
import com.tm.common.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("projectService")
public class ProjectService extends BaseService {
    @Autowired
    private ProjectMapper projectMapper;

    public BaseResponse getProjectReportInfo(Integer projectId) {
        ProjectStatisticsInfo projectStatisticsInfo = new ProjectStatisticsInfo();
        projectStatisticsInfo.setTotalCase(projectMapper.getCountCase(projectId));
        projectStatisticsInfo.setTotalPlan(projectMapper.getCountPlan(projectId));
        return ResultUtils.success(projectStatisticsInfo);
    }
}
