package com.tm.web.service;

import com.tm.common.entities.autotest.request.RunPlanBody;
import com.tm.common.entities.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "tm-worker-service")
public interface AutoTestFeignService {
    @PostMapping(value = "/tm/public/api/autotest/runPlan", produces = {"application/json;charset=UTF-8"})
    BaseResponse runPlan(@RequestBody @Valid RunPlanBody body);
}
