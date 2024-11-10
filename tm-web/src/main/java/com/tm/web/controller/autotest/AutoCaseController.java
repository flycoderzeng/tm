package com.tm.web.controller.autotest;

import com.tm.common.base.model.AutoCase;
import com.tm.common.base.model.User;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.IdBody;
import com.tm.web.controller.BaseController;
import com.tm.web.service.AutoCaseService;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/autocase")
public class AutoCaseController extends BaseController {
    private final AutoCaseService autoCaseService;

    @Inject
    public AutoCaseController(AutoCaseService autoCaseService) {
        this.autoCaseService = autoCaseService;
    }

    @PostMapping(value = "/save", produces = {"application/json;charset=UTF-8"})
    public BaseResponse save(@RequestBody AutoCase autoCase) {
        User user = this.getLoginUser();
        return autoCaseService.saveAutoCase(autoCase, user);
    }

    @PostMapping(value = "/load", produces = {"application/json;charset=UTF-8"})
    public BaseResponse load(@RequestBody IdBody body) {
        return autoCaseService.loadAutoCase(body.getId());
    }
}
