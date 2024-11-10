package com.tm.common.dao;

import com.tm.common.base.mapper.PlanRunningConfigSnapshotMapper;
import com.tm.common.base.model.PlanRunningConfigSnapshot;
import jakarta.inject.Inject;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class PlanRunningConfigSnapshotDao {
    private final PlanRunningConfigSnapshotMapper planRunningConfigSnapshotMapper;

    @Inject
    public PlanRunningConfigSnapshotDao(PlanRunningConfigSnapshotMapper planRunningConfigSnapshotMapper) {
        Assert.notNull(planRunningConfigSnapshotMapper, "PlanRunningConfigSnapshotMapper must not be null!");
        this.planRunningConfigSnapshotMapper = planRunningConfigSnapshotMapper;
    }

    public int insertBySelective(PlanRunningConfigSnapshot record) {
        return planRunningConfigSnapshotMapper.insertBySelective(record);
    }
}
