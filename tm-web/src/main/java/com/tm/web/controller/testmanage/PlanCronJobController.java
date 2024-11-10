package com.tm.web.controller.testmanage;

import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.testmanage.CheckExpressionBody;
import com.tm.web.controller.BaseController;
import com.tm.web.service.PlanCronJobService;
import jakarta.inject.Inject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/cronjob")
public class PlanCronJobController extends BaseController {
    private final PlanCronJobService planCronJobService;

    @Inject
    public PlanCronJobController(PlanCronJobService planCronJobService) {
        this.planCronJobService = planCronJobService;
    }

    @PostMapping(value = "/checkCronExpression", produces = {"application/json;charset=UTF-8"})
    public BaseResponse checkCronExpression(@RequestBody @Valid CheckExpressionBody body) {
        return planCronJobService.checkCronExpression(body.getExpression());
    }
}
