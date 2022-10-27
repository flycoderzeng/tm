package com.tm.web.controller.mock;

import com.tm.common.base.model.HttpMockRule;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.CommonTableQueryBody;
import com.tm.common.entities.common.CommonIdBody;
import com.tm.web.controller.BaseController;
import com.tm.web.service.MockRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/mock")
public class MockRuleController extends BaseController {
    @Autowired
    private MockRuleService mockRuleService;

    @PostMapping(value = "/saveHttpMockRule", produces = {"application/json;charset=UTF-8"})
    public BaseResponse saveHttpMockRule(@RequestBody @Valid HttpMockRule rule) {
        return mockRuleService.saveHttpMockRule(rule, getLoginUser());
    }

    @PostMapping(value = "/deleteHttpMockRule", produces = {"application/json;charset=UTF-8"})
    public BaseResponse deleteHttpMockRule(@RequestBody @Valid CommonIdBody body) {
        return mockRuleService.deleteHttpMockRule(body.getId(), getLoginUser());
    }

    @PostMapping(value = "/loadHttpMockRule", produces = {"application/json;charset=UTF-8"})
    public BaseResponse loadHttpMockRule(@RequestBody @Valid CommonIdBody body) {
        return mockRuleService.loadHttpMockRule(body.getId());
    }

    @PostMapping(value = "/queryHttpMockRuleList", produces = {"application/json;charset=UTF-8"})
    public BaseResponse queryHttpMockRuleList(@RequestBody @Valid CommonTableQueryBody body) {
        body.setLoginUsername(getLoginUser().getUsername());
        return mockRuleService.queryHttpMockRuleList(body);
    }
}
