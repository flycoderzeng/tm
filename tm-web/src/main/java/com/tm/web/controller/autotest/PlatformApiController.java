package com.tm.web.controller.autotest;


import com.tm.common.base.model.PlatformApi;
import com.tm.common.base.model.User;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.IdBody;
import com.tm.common.utils.ResultUtils;
import com.tm.web.controller.BaseController;
import com.tm.web.service.PlatformApiService;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/platformapi")
public class PlatformApiController extends BaseController {
    private final PlatformApiService platformApiService;

    @Inject
    public PlatformApiController(PlatformApiService platformApiService) {
        this.platformApiService = platformApiService;
    }

    @PostMapping(value = "/load", produces = {"application/json;charset=UTF-8"})
    public BaseResponse load(@RequestBody @Valid IdBody body) {
        return ResultUtils.success(platformApiService.load(body.getId()));
    }

    @PostMapping(value = "/save", produces = {"application/json;charset=UTF-8"})
    public BaseResponse save(@RequestBody @Valid PlatformApi platformApi) {
        User user = this.getLoginUser();
        return ResultUtils.success(platformApiService.update(platformApi, user));
    }

    @GetMapping(value = "/getTree")
    public BaseResponse getTree() {
        return ResultUtils.success(platformApiService.getTree());
    }
}
