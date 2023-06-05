package com.tm.web.controller.autotest;

import com.tm.common.base.model.AutoCase;
import com.tm.common.entities.base.BaseResponse;
import com.tm.web.controller.BaseController;
import com.tm.web.service.DbConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/dbconfig")
public class DbConfigController extends BaseController {
    @Autowired
    private DbConfigService dbConfigService;

    @GetMapping(value = "/getAllDatabaseNames")
    public BaseResponse getAllDatabaseNames() {
        return dbConfigService.getAllDatabaseNames();
    }
}
