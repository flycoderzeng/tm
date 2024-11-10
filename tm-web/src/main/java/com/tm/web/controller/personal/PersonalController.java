package com.tm.web.controller.personal;

import com.tm.common.base.mapper.ProjectMapper;
import com.tm.common.base.model.Project;
import com.tm.common.base.model.User;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.utils.ResultUtils;
import com.tm.web.controller.BaseController;
import com.tm.web.service.ProjectService;
import jakarta.inject.Inject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/personal")
public class PersonalController extends BaseController {
    private final ProjectMapper projectMapper;
    private final ProjectService projectService;

    @Inject
    public PersonalController(ProjectMapper projectMapper, ProjectService projectService) {
        this.projectMapper = projectMapper;
        this.projectService = projectService;
    }

    @GetMapping(value = "/getMyProjects")
    public BaseResponse getMyProjects() {
        User user = this.getLoginUser();
        List<Project> userProjects = projectMapper.getUserProjects(user.getId());
        for (Project userProject : userProjects) {
            userProject.setProjectStatisticsInfo(projectService.getProjectReportInfo(userProject.getId()));
        }
        return ResultUtils.success(userProjects);
    }
}
