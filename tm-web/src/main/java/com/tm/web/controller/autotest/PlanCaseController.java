package com.tm.web.controller.autotest;

import com.tm.common.entities.autotest.request.AddCaseToPlanBody;
import com.tm.common.entities.autotest.request.ChangePlanCaseSeqBody;
import com.tm.common.entities.autotest.request.DeletePlanCaseBody;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.CommonTableQueryBody;
import com.tm.common.entities.common.CommonIdBody;
import com.tm.common.utils.ResultUtils;
import com.tm.web.service.PlanCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/plancase")
public class PlanCaseController {
    @Autowired
    private PlanCaseService planCaseService;

    @PostMapping(value = "/queryList", produces = {"application/json;charset=UTF-8"})
    public BaseResponse queryList(@RequestBody @Valid CommonTableQueryBody body) {
        return ResultUtils.success(planCaseService.queryList(body));
    }

    @PostMapping(value = "/addCaseToPlan", produces = {"application/json;charset=UTF-8"})
    public BaseResponse addCaseToPlan(@RequestBody @Valid AddCaseToPlanBody body) {
        planCaseService.addCaseToPlan(body);
        return ResultUtils.success();
    }

    @PostMapping(value = "/deletePlanCase", produces = {"application/json;charset=UTF-8"})
    public BaseResponse deletePlanCase(@RequestBody @Valid DeletePlanCaseBody body) {
        planCaseService.deletePlanCase(body.getPlanId(), body.getIdList());
        return ResultUtils.success();
    }

    @PostMapping(value = "/clearPlanCase", produces = {"application/json;charset=UTF-8"})
    public BaseResponse clearPlanCase(@RequestBody @Valid CommonIdBody body) {
        planCaseService.clearPlanCase(body.getId());
        return ResultUtils.success();
    }

    @PostMapping(value = "/changeCaseSeq", produces = {"application/json;charset=UTF-8"})
    public BaseResponse changeCaseSeq(@RequestBody @Valid ChangePlanCaseSeqBody body) {
        planCaseService.changeCaseSeq(body.getPlanId(), body.getCaseId(), body.getSeq());
        return ResultUtils.success();
    }
}
