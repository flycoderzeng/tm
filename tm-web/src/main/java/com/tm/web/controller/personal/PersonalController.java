package com.tm.web.controller.personal;

import com.tm.common.base.mapper.ProjectMapper;
import com.tm.common.base.model.User;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.utils.ResultUtils;
import com.tm.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/personal")
public class PersonalController extends BaseController {
    @Autowired
    private ProjectMapper projectMapper;

    @GetMapping(value = "/getMyProjects")
    public BaseResponse getMyProjects() {
        User user = this.getLoginUser();
        return ResultUtils.success(projectMapper.getUserProjects(user.getId()));
    }
}
