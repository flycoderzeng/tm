package com.tm.web.controller.autotest;

import com.tm.common.base.model.AutoPlan;
import com.tm.common.base.model.User;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.IdBody;
import com.tm.web.controller.BaseController;
import com.tm.web.service.AutoPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/autoplan")
public class AutoPlanController extends BaseController {
    @Autowired
    private AutoPlanService autoPlanService;

    @PostMapping(value = "/load", produces = {"application/json;charset=UTF-8"})
    public BaseResponse load(@RequestBody IdBody body) {
        return autoPlanService.load(body.getId());
    }

    @PostMapping(value = "/save", produces = {"application/json;charset=UTF-8"})
    public BaseResponse save(@RequestBody AutoPlan autoPlan) {
        User user = getLoginUser();
        return autoPlanService.save(autoPlan, user);
    }

    @PostMapping(value = "/savePlanVariables", produces = {"application/json;charset=UTF-8"})
    public BaseResponse savePlanVariables(@RequestBody AutoPlan autoPlan) {
        User user = getLoginUser();
        return autoPlanService.savePlanVariables(autoPlan, user);
    }
}
