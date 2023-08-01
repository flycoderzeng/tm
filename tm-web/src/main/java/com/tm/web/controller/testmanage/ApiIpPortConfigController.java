package com.tm.web.controller.testmanage;

import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.common.CommonIdBody;
import com.tm.common.entities.testmanage.BatchCopyCommonConfigBody;
import com.tm.web.controller.BaseController;
import com.tm.web.service.ApiIpPortConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/apiipportconfig")
public class ApiIpPortConfigController extends BaseController {
    @Autowired
    private ApiIpPortConfigService apiIpPortConfigService;

    @PostMapping(value = "/batchCopyApiIpPortConfig", produces = {"application/json;charset=UTF-8"})
    public BaseResponse batchCopyApiIpPortConfig(@RequestBody @Valid BatchCopyCommonConfigBody body) {
        return apiIpPortConfigService.batchCopyApiIpPortConfig(body, getLoginUser());
    }

    @PostMapping(value = "/setDcnIdToNull", produces = {"application/json;charset=UTF-8"})
    public BaseResponse setDcnIdToNull(@RequestBody @Valid CommonIdBody body) {
        return apiIpPortConfigService.setDcnIdToNull(body.getId());
    }
}
