package com.tm.web.controller.testmanage;

import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.common.CommonIdBody;
import com.tm.common.entities.testmanage.BatchCopyCommonConfigBody;
import com.tm.web.controller.BaseController;
import com.tm.web.service.DbConfigService;
import jakarta.inject.Inject;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/dbconfig")
public class DbConfigController extends BaseController {
    private final DbConfigService dbConfigService;

    @Inject
    public DbConfigController(DbConfigService dbConfigService) {
        this.dbConfigService = dbConfigService;
    }

    @PostMapping(value = "/batchCopyDbConfig", produces = {"application/json;charset=UTF-8"})
    public BaseResponse batchCopyDbConfig(@RequestBody @Valid BatchCopyCommonConfigBody body) {
        return dbConfigService.batchCopyDbConfig(body, getLoginUser());
    }

    @GetMapping(value = "/getAllDatabaseNames")
    public BaseResponse getAllDatabaseNames() {
        return dbConfigService.getAllDatabaseNames();
    }

    @PostMapping(value = "/setDcnIdToNull", produces = {"application/json;charset=UTF-8"})
    public BaseResponse setDcnIdToNull(@RequestBody @Valid CommonIdBody body) {
        return dbConfigService.setDcnIdToNull(body.getId());
    }
}
