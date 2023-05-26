package com.tm.common.base.mapper;

import com.tm.common.base.model.DistributedLock;

public interface DistributedLockMapper {
    int insertBySelective(DistributedLock record);
    int deleteByPrimaryKey(DistributedLock record);
    DistributedLock selectByPrimaryKey(DistributedLock record);
}
