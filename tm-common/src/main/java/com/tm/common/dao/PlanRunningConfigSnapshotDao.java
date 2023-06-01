package com.tm.common.dao;

import com.tm.common.base.mapper.PlanRunningConfigSnapshotMapper;
import com.tm.common.base.model.PlanRunningConfigSnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlanRunningConfigSnapshotDao {
    @Autowired
    private PlanRunningConfigSnapshotMapper planRunningConfigSnapshotMapper;

    public int insertBySelective(PlanRunningConfigSnapshot record) {
        return planRunningConfigSnapshotMapper.insertBySelective(record);
    }
}
