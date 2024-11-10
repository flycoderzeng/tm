package com.tm.web.controller.autotest;

import com.tm.common.entities.autotest.request.GetPlanRunResultStatusBody;
import com.tm.common.entities.autotest.request.RetryFailedCaseBody;
import com.tm.common.entities.autotest.request.RunCaseBody;
import com.tm.common.entities.autotest.request.RunPlanBody;
import com.tm.common.entities.base.BaseResponse;
import jakarta.inject.Inject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/autotest")
public class AutoTestFeignController {
    private final AutoTestFeignInterface autoTestFeignInterface;

    @Inject
    public AutoTestFeignController(AutoTestFeignInterface autoTestFeignInterface) {
        this.autoTestFeignInterface = autoTestFeignInterface;
    }

    @PostMapping(value = "/runCase")
    public BaseResponse runAutoCase(@RequestBody @Valid RunCaseBody body) {
        return autoTestFeignInterface.runAutoCase(body);
    }

    @PostMapping(value = "/runPlan", produces = {"application/json;charset=UTF-8"})
    public BaseResponse runPlan(@RequestBody @Valid RunPlanBody body) {
        return autoTestFeignInterface.runPlan(body);
    }

    @PostMapping(value = "/getPlanRunResultStatus", produces = {"application/json;charset=UTF-8"})
    public BaseResponse getPlanRunResultStatus(@RequestBody @Valid GetPlanRunResultStatusBody body) {
        return autoTestFeignInterface.getPlanRunResultStatus(body);
    }

    @PostMapping(value = "/retryFailedCase", produces = {"application/json;charset=UTF-8"})
    public BaseResponse retryFailedCase(@RequestBody @Valid RetryFailedCaseBody body) {
        return autoTestFeignInterface.retryFailedCase(body);
    }
}
