package com.tm.web.service;

import com.tm.common.base.mapper.MockAgentInstanceMapper;
import com.tm.common.base.model.MockAgentInstance;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.CommonTableQueryBody;
import com.tm.common.entities.base.CommonTableQueryResponse;
import com.tm.common.utils.ResultUtils;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MockInstanceService {
    private final MockAgentInstanceMapper mockAgentInstanceMapper;

    @Inject
    public MockInstanceService(MockAgentInstanceMapper mockAgentInstanceMapper) {
        this.mockAgentInstanceMapper = mockAgentInstanceMapper;
    }

    public BaseResponse queryInstanceList(CommonTableQueryBody body) {
        List<MockAgentInstance> mockAgentInstances = new ArrayList<>();
        final int total = mockAgentInstanceMapper.countList(body);
        if(total > 0) {
            mockAgentInstances = mockAgentInstanceMapper.queryList(body);
        }
        return ResultUtils.success(new CommonTableQueryResponse<>(mockAgentInstances, total));
    }
}
