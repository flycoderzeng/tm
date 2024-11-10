package com.tm.web.controller.autotest;

import com.tm.common.base.model.HttpApi;
import com.tm.common.base.model.User;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.IdBody;
import com.tm.common.utils.ResultUtils;
import com.tm.web.controller.BaseController;
import com.tm.web.service.HttpApiService;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/httpapi")
public class HttpApiController extends BaseController {
    private final HttpApiService httpApiService;

    @Inject
    public HttpApiController(HttpApiService httpApiService) {
        this.httpApiService = httpApiService;
    }

    @GetMapping(value = "/getHttpMethodList")
    public BaseResponse getScriptTypeList() {
        return ResultUtils.success(httpApiService.getHttpMethodList());
    }

    @PostMapping(value = "/load", produces = {"application/json;charset=UTF-8"})
    public BaseResponse load(@RequestBody @Valid IdBody body) {
        return ResultUtils.success(httpApiService.load(body.getId()));
    }

    @PostMapping(value = "/save", produces = {"application/json;charset=UTF-8"})
    public BaseResponse save(@RequestBody @Valid HttpApi httpApi) {
        User user = this.getLoginUser();
        return httpApiService.update(httpApi, user);
    }
}
