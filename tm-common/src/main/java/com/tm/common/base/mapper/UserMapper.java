package com.tm.common.base.mapper;

import com.tm.common.base.model.User;
import com.tm.common.entities.base.CommonTableQueryBody;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UserMapper {
    User getUserByName(@Param("username") String username);
    User getUserByNameWithPassword(@Param("username") String username);
}