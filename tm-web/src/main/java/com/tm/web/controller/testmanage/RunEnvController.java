package com.tm.web.controller.testmanage;

import com.tm.common.base.model.RunEnv;
import com.tm.common.base.model.User;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.CommonTableQueryBody;
import com.tm.common.entities.base.IdBody;
import com.tm.web.controller.BaseController;
import com.tm.web.service.RunEnvService;
import com.tm.common.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/runenv")
public class RunEnvController extends BaseController {
    @Autowired
    private RunEnvService runEnvService;

    @PostMapping(value = "/delete" ,produces = {"application/json;charset=UTF-8"})
    public BaseResponse delete(@RequestBody @Valid IdBody body) {
        return ResultUtils.success(runEnvService.delete(body.getId()));
    }

    @PostMapping(value = "/load" ,produces = {"application/json;charset=UTF-8"})
    public BaseResponse load(@RequestBody @Valid IdBody body) {
        return ResultUtils.success(runEnvService.load(body.getId()));
    }

    @PostMapping(value = "/queryList", produces = {"application/json;charset=UTF-8"})
    public BaseResponse queryCronJobList(@RequestBody @Valid CommonTableQueryBody body) {
        return ResultUtils.success(runEnvService.queryList(body));
    }

    @PostMapping(value = "/save", produces = {"application/json;charset=UTF-8"})
    public BaseResponse save(@RequestBody RunEnv body) {
        User user = this.getLoginUser();
        return ResultUtils.success(runEnvService.save(body, user));
    }

    @GetMapping(value = "/getAllRunEnv")
    public BaseResponse getAllRunEnv() {
        return ResultUtils.success(runEnvService.getAllRunEnv());
    }
}
