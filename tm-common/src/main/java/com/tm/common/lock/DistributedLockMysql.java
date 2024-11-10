package com.tm.common.lock;

import com.tm.common.base.mapper.DistributedLockMapper;
import com.tm.common.base.model.DistributedLock;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Slf4j
@Component
public class DistributedLockMysql {
    public static final Long MAX_LOCK_TIME = 7200000L;
    private final DistributedLockMapper distributedLockMapper;

    @Inject
    public DistributedLockMysql(DistributedLockMapper distributedLockMapper) {
        Assert.notNull(distributedLockMapper, "DistributedLockMapper must not be null!");
        this.distributedLockMapper = distributedLockMapper;
    }

    public boolean lock(Integer id, String name) {
        final DistributedLock distributedLock = new DistributedLock(id, name);
        try {
            distributedLockMapper.insertBySelective(distributedLock);
        } catch (Exception e0) {
            log.info("加锁失败，id: {}, name: {}", id, name);
            final DistributedLock lock = distributedLockMapper.selectByPrimaryKey(distributedLock);
            if(lock.getAddTime() != null &&
                    (System.currentTimeMillis() - lock.getAddTime().getTime()) > MAX_LOCK_TIME) {
                log.info("锁的时间超过最大锁时长配置，删除锁，id: {}, name: {}", id, name);
                distributedLockMapper.deleteByPrimaryKey(distributedLock);
            }
            try {
                distributedLockMapper.insertBySelective(distributedLock);
                return true;
            } catch (Exception e1) {
                log.info("加锁失败，id: {}, name: {}", id, name);
                return false;
            }
        }

        return true;
    }

    public void unlock(Integer id, String name) {
        distributedLockMapper.deleteByPrimaryKey(new DistributedLock(id, name));
    }
}
