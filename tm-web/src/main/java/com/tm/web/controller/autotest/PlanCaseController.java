package com.tm.web.controller.autotest;

import com.tm.common.entities.autotest.request.AddCaseToPlanBody;
import com.tm.common.entities.autotest.request.ChangePlanCaseSeqBody;
import com.tm.common.entities.autotest.request.ClearPlanCaseBody;
import com.tm.common.entities.autotest.request.DeletePlanCaseBody;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.CommonTableQueryBody;
import com.tm.common.utils.ResultUtils;
import com.tm.web.service.PlanCaseService;
import jakarta.inject.Inject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/plancase")
public class PlanCaseController {
    private final PlanCaseService planCaseService;

    @Inject
    public PlanCaseController(PlanCaseService planCaseService) {
        this.planCaseService = planCaseService;
    }

    @PostMapping(value = "/queryList", produces = {"application/json;charset=UTF-8"})
    public BaseResponse queryList(@RequestBody @Valid CommonTableQueryBody body) {
        return ResultUtils.success(planCaseService.queryList(body));
    }

    @PostMapping(value = "/addCaseToPlan", produces = {"application/json;charset=UTF-8"})
    public BaseResponse addCaseToPlan(@RequestBody @Valid AddCaseToPlanBody body) {
        planCaseService.addCaseToPlan(body);
        return ResultUtils.success();
    }

    @PostMapping(value = "/addCaseTreeToPlan", produces = {"application/json;charset=UTF-8"})
    public BaseResponse addCaseTreeToPlan(@RequestBody @Valid AddCaseToPlanBody body) {
        planCaseService.addCaseTreeToPlan(body);
        return ResultUtils.success();
    }

    @PostMapping(value = "/deletePlanCase", produces = {"application/json;charset=UTF-8"})
    public BaseResponse deletePlanCase(@RequestBody @Valid DeletePlanCaseBody body) {
        planCaseService.deletePlanCase(body);
        return ResultUtils.success();
    }

    @PostMapping(value = "/clearPlanCase", produces = {"application/json;charset=UTF-8"})
    public BaseResponse clearPlanCase(@RequestBody @Valid ClearPlanCaseBody body) {
        planCaseService.clearPlanCase(body);
        return ResultUtils.success();
    }

    @PostMapping(value = "/changeCaseSeq", produces = {"application/json;charset=UTF-8"})
    public BaseResponse changeCaseSeq(@RequestBody @Valid ChangePlanCaseSeqBody body) {
        planCaseService.changeCaseSeq(body.getPlanId(), body.getCaseId(), body.getSeq(), body.getType());
        return ResultUtils.success();
    }
}
