package com.tm.web.controller.autotest;

import com.tm.common.base.model.GlobalVariable;
import com.tm.common.base.model.User;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.IdBody;
import com.tm.common.utils.ResultUtils;
import com.tm.web.controller.BaseController;
import com.tm.web.service.GlobalVariableService;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/globalvariable")
public class GlobalVariableController extends BaseController {
    private final GlobalVariableService globalVariableService;

    @Inject
    public GlobalVariableController(GlobalVariableService globalVariableService) {
        this.globalVariableService = globalVariableService;
    }

    @PostMapping(value = "/load", produces = {"application/json;charset=UTF-8"})
    public BaseResponse load(@RequestBody @Valid IdBody body) {
        return ResultUtils.success(globalVariableService.load(body.getId()));
    }

    @PostMapping(value = "/save", produces = {"application/json;charset=UTF-8"})
    public BaseResponse save(@RequestBody @Valid GlobalVariable globalVariable) {
        User user = this.getLoginUser();
        return globalVariableService.update(globalVariable, user);
    }
}
