package com.tm.web.controller.autotest;

import com.tm.common.entities.autotest.request.GetPlanRunResultStatusBody;
import com.tm.common.entities.autotest.request.RetryFailedCaseBody;
import com.tm.common.entities.autotest.request.RunCaseBody;
import com.tm.common.entities.autotest.request.RunPlanBody;
import com.tm.common.entities.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "tm-worker-service")
public interface AutoTestFeignInterface {
    @PostMapping(value="/autotest/runCase")
    BaseResponse runAutoCase(@RequestBody @Valid RunCaseBody body);

    @PostMapping(value = "/autotest/runPlan", produces = {"application/json;charset=UTF-8"})
    BaseResponse runPlan(@RequestBody @Valid RunPlanBody body);

    @PostMapping(value = "/autotest/getPlanRunResultStatus", produces = {"application/json;charset=UTF-8"})
    BaseResponse getPlanRunResultStatus(@RequestBody @Valid GetPlanRunResultStatusBody body);

    @PostMapping(value = "/autotest/retryFailedCase", produces = {"application/json;charset=UTF-8"})
    BaseResponse retryFailedCase(@RequestBody @Valid RetryFailedCaseBody body);

    @PostMapping(value = "/tm/public/api/autotest/runPlan", produces = {"application/json;charset=UTF-8"})
    BaseResponse runPlanPublic(@RequestBody @Valid RunPlanBody body);
}
