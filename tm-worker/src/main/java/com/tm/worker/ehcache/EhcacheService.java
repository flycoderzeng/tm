package com.tm.worker.ehcache;


public interface EhcacheService {
    <T> T get(String key);
    void put(String key, Object data);
}
