package com.tm.web.controller.mock;

import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.CommonTableQueryBody;
import com.tm.web.controller.BaseController;
import com.tm.web.service.MockInstanceService;
import jakarta.inject.Inject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/mock")
public class MockInstanceController extends BaseController {
    private final MockInstanceService mockInstanceService;

    @Inject
    public MockInstanceController(MockInstanceService mockInstanceService) {
        this.mockInstanceService = mockInstanceService;
    }

    @PostMapping(value = "/queryInstanceList", produces = {"application/json;charset=UTF-8"})
    public BaseResponse queryInstanceList(@RequestBody @Valid CommonTableQueryBody body) {
        return mockInstanceService.queryInstanceList(body);
    }
}
