package com.tm.web.controller.personal;

import com.tm.common.base.mapper.ProjectMapper;
import com.tm.common.base.model.User;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.common.CommonIdBody;
import com.tm.common.utils.ResultUtils;
import com.tm.web.controller.BaseController;
import com.tm.web.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/personal")
public class PersonalController extends BaseController {
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ProjectService projectService;

    @GetMapping(value = "/getMyProjects")
    public BaseResponse getMyProjects() {
        User user = this.getLoginUser();
        return ResultUtils.success(projectMapper.getUserProjects(user.getId()));
    }

    @PostMapping(value = "/getProjectReportInfo", produces = {"application/json;charset=UTF-8"})
    public BaseResponse getProjectReportInfo(@RequestBody @Valid CommonIdBody body) {
        return projectService.getProjectReportInfo(body.getId());
    }
}
