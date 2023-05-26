package com.tm.worker.controller;

import com.tm.common.base.model.User;
import com.tm.common.entities.autotest.request.RunCaseBody;
import com.tm.common.entities.autotest.request.RunPlanBody;
import com.tm.common.entities.base.BaseResponse;
import com.tm.worker.service.AutoTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/autotest")
public class AutoTestController extends BaseController {
    @Autowired
    private AutoTestService autoTestService;

    @PostMapping(value = "/runCase")
    public BaseResponse runAutoCase(@RequestBody @Valid RunCaseBody body) {
        User user = getLoginUser();
        BaseResponse baseResponse = autoTestService.runAutoCase(body, user);
        return baseResponse;
    }

    @PostMapping(value = "/runPlan", produces = {"application/json;charset=UTF-8"})
    public BaseResponse runPlan(@RequestBody @Valid RunPlanBody body) {
        User user = getLoginUser();
        BaseResponse baseResponse = autoTestService.runAutoPlan(body, user);
        return baseResponse;
    }
}
