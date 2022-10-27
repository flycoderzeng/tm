package com.tm.web.controller.autotest;

import com.tm.web.controller.BaseController;
import com.tm.common.base.model.AutoScript;
import com.tm.common.base.model.User;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.IdBody;
import com.tm.web.service.AutoShellService;
import com.tm.common.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/autoscript")
public class AutoScriptController extends BaseController {
    @Autowired
    private AutoShellService autoShellService;

    @GetMapping(value = "/getScriptTypeList")
    public BaseResponse getScriptTypeList() {
        return ResultUtils.success(autoShellService.getScriptTypeList());
    }

    @PostMapping(value = "/load", produces = {"application/json;charset=UTF-8"})
    public BaseResponse load(@RequestBody @Valid IdBody body) {
        return ResultUtils.success(autoShellService.load(body.getId()));
    }

    @PostMapping(value = "/save", produces = {"application/json;charset=UTF-8"})
    public BaseResponse save(@RequestBody @Valid AutoScript autoScript) {
        User user = this.getLoginUser();
        return ResultUtils.success(autoShellService.update(autoScript, user));
    }
}
