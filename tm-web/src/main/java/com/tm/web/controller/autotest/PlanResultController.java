package com.tm.web.controller.autotest;

import com.tm.common.entities.autotest.request.GetNewestPlanExecuteResultBody;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.CommonTableQueryBody;
import com.tm.common.entities.base.IdBody;
import com.tm.web.controller.BaseController;
import com.tm.web.service.PlanResultService;
import jakarta.inject.Inject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/planresult")
public class PlanResultController extends BaseController {

    private final PlanResultService planResultService;

    @Inject
    public PlanResultController(PlanResultService planResultService) {
        this.planResultService = planResultService;
    }

    @PostMapping(value = "/getNewestPlanExecuteResult", produces = {"application/json;charset=UTF-8"})
    public BaseResponse getNewestPlanExecuteResult(@RequestBody @Valid GetNewestPlanExecuteResultBody body) {
        return planResultService.getNewestPlanExecuteResult(body);
    }

    @PostMapping(value = "/getSinglePlanExecuteResult", produces = {"application/json;charset=UTF-8"})
    public BaseResponse getSinglePlanExecuteResult(@RequestBody @Valid IdBody body) {
        return planResultService.getSinglePlanExecuteResult(body.getId());
    }

    @PostMapping(value = "/getPlanHistoryExecuteResultList", produces = {"application/json;charset=UTF-8"})
    public BaseResponse getPlanHistoryExecuteResultList(@RequestBody @Valid CommonTableQueryBody body) {
        return planResultService.getPlanHistoryExecuteResultList(body);
    }

    @PostMapping(value = "/getPlanCaseExecuteResultList", produces = {"application/json;charset=UTF-8"})
    public BaseResponse getPlanCaseExecuteResultList(@RequestBody @Valid CommonTableQueryBody body) {
        return planResultService.getPlanCaseExecuteResultList(body);
    }

    @PostMapping(value = "/getCaseVariableResultList", produces = {"application/json;charset=UTF-8"})
    public BaseResponse getCaseVariableResultList(@RequestBody @Valid CommonTableQueryBody body) {
        return planResultService.getCaseVariableResultList(body);
    }

    @PostMapping(value = "/getCaseStepResultList", produces = {"application/json;charset=UTF-8"})
    public BaseResponse getCaseStepResultList(@RequestBody @Valid CommonTableQueryBody body) {
        return planResultService.getCaseStepResultList(body);
    }
}
