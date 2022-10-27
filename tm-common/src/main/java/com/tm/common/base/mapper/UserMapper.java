package com.tm.common.base.mapper;

import com.tm.common.base.model.User;
import com.tm.common.entities.base.CommonTableQueryBody;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UserMapper {
    User getUserByName(@Param("username") String username);
    User getUserByNameWithPassword(@Param("username") String username);
    List<User> queryList(CommonTableQueryBody body);
    int countList(CommonTableQueryBody body);
    User findById(Integer id);
    int updateBySelective(User user);
    int insertBySelective(User user);
    int deleteByPrimaryKey(int id);
}