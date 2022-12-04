package com.tm.common.base.mapper;

import com.tm.common.base.model.User;
import org.apache.ibatis.annotations.Param;


public interface UserMapper {
    User getUserByName(@Param("username") String username);
    User getUserByNameWithPassword(@Param("username") String username);
}