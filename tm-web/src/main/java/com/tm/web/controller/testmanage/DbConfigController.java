package com.tm.web.controller.testmanage;

import com.tm.common.base.model.DbConfig;
import com.tm.common.base.model.User;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.CommonTableQueryBody;
import com.tm.common.entities.base.IdBody;
import com.tm.web.controller.BaseController;
import com.tm.web.service.DbConfigService;
import com.tm.common.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/dbconfig")
public class DbConfigController extends BaseController {
    @Autowired
    private DbConfigService dbConfigService;

    @PostMapping(value = "/delete" ,produces = {"application/json;charset=UTF-8"})
    public BaseResponse delete(@RequestBody @Valid IdBody body) {
        return ResultUtils.success(dbConfigService.delete(body.getId()));
    }

    @PostMapping(value = "/load" ,produces = {"application/json;charset=UTF-8"})
    public BaseResponse load(@RequestBody @Valid IdBody body) {
        return ResultUtils.success(dbConfigService.load(body.getId()));
    }

    @PostMapping(value = "/queryList", produces = {"application/json;charset=UTF-8"})
    public BaseResponse queryCronJobList(@RequestBody @Valid CommonTableQueryBody body) {
        User user = getLoginUser();
        return ResultUtils.success(dbConfigService.queryList(body, user));
    }

    @PostMapping(value = "/save", produces = {"application/json;charset=UTF-8"})
    public BaseResponse save(@RequestBody @Valid DbConfig body) {
        User user = this.getLoginUser();
        return ResultUtils.success(dbConfigService.save(body, user));
    }
}
