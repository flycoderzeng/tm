package com.tm.worker.controller;

import com.tm.common.base.model.User;
import com.tm.common.entities.autotest.request.GetPlanRunResultStatusBody;
import com.tm.common.entities.autotest.request.RunCaseBody;
import com.tm.common.entities.autotest.request.RunPlanBody;
import com.tm.common.entities.base.BaseResponse;
import com.tm.worker.service.AutoTestService;
import jakarta.inject.Inject;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/tm/public/api/")
public class PublicController extends BaseController {
    private final AutoTestService autoTestService;

    @Inject
    public PublicController(AutoTestService autoTestService) {
        Assert.notNull(autoTestService, "AutoTestService must not be null!");
        this.autoTestService = autoTestService;
    }

    @PostMapping(value = "/autotest/runCase")
    public BaseResponse runAutoCase(@RequestBody @Valid RunCaseBody body) {
        User user = getLoginUser();
        user.setUsername("定时任务");
        return autoTestService.runAutoCase(body, user);
    }

    @PostMapping(value = "/autotest/runPlan", produces = {"application/json;charset=UTF-8"})
    public BaseResponse runPlan(@RequestBody @Valid RunPlanBody body) {
        User user = getLoginUser();
        user.setUsername("定时任务");
        return autoTestService.runAutoPlan(body, user);
    }

    @PostMapping(value = "/autotest/getPlanRunResultStatus", produces = {"application/json;charset=UTF-8"})
    public BaseResponse getPlanRunResultStatus(@RequestBody @Valid GetPlanRunResultStatusBody body) {
        User user = getLoginUser();
        return autoTestService.getPlanRunResultStatus(body, user);
    }
}
