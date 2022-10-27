package com.tm.web.controller.testmanage;

import com.tm.common.base.model.ApiIpPortConfig;
import com.tm.common.base.model.User;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.CommonTableQueryBody;
import com.tm.common.entities.base.IdBody;
import com.tm.web.controller.BaseController;
import com.tm.web.service.ApiIpPortConfigService;
import com.tm.common.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/urlconfig")
public class ApiIpPortConfigController extends BaseController {
    @Autowired
    private ApiIpPortConfigService apiIpPortConfigService;

    @PostMapping(value = "/delete" ,produces = {"application/json;charset=UTF-8"})
    public BaseResponse delete(@RequestBody @Valid IdBody body) {
        return ResultUtils.success(apiIpPortConfigService.delete(body.getId()));
    }

    @PostMapping(value = "/load" ,produces = {"application/json;charset=UTF-8"})
    public BaseResponse load(@RequestBody @Valid IdBody body) {
        return ResultUtils.success(apiIpPortConfigService.load(body.getId()));
    }

    @PostMapping(value = "/queryList", produces = {"application/json;charset=UTF-8"})
    public BaseResponse queryCronJobList(@RequestBody @Valid CommonTableQueryBody body) {
        User user = getLoginUser();
        return ResultUtils.success(apiIpPortConfigService.queryList(body, user));
    }

    @PostMapping(value = "/save", produces = {"application/json;charset=UTF-8"})
    public BaseResponse save(@RequestBody ApiIpPortConfig body) {
        User user = this.getLoginUser();
        return ResultUtils.success(apiIpPortConfigService.save(body, user));
    }
}
