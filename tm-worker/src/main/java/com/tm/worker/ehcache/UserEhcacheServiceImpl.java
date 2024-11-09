package com.tm.worker.ehcache;


import com.tm.common.base.mapper.UserMapper;
import com.tm.common.base.model.User;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;


@Service("ehcacheService")
public class UserEhcacheServiceImpl implements EhcacheService {
    private static Logger logger = LoggerFactory.getLogger(UserEhcacheServiceImpl.class);
    private Cache cache;
    public static final String UserPrivilegesCache = "UserPrivilegesCache";
    @Autowired
    private UserMapper userMapper;
    @PostConstruct
    public void init() {
        CacheManager cacheManager;
        try {
            cacheManager = CacheManager.create(new ClassPathResource("ehcache.xml").getInputStream());
        } catch (IOException e) {
            logger.error("init error: ", e);
            return ;
        }
        cache = cacheManager.getCache(UserPrivilegesCache);
    }

    @Override
    public User get(String key) {
        Element value = cache.get(key);
        if (value == null) {
            // 从数据库读取
            User user = userMapper.getUserByName(key);
            if (user == null) {
                // 用户非法
                return null;
            }
            put(key, user);
            return user;
        }
        return (User)value.getObjectValue();
    }

    @Override
    public void put(String key, Object user) {
        Element element = new Element(key, user);
        cache.put(element);
    }
}
