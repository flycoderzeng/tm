package com.tm.common.base.mapper;



import com.tm.common.base.model.UserRole;
import com.tm.common.base.model.UserRoleRelation;

import java.util.List;

public interface UserRoleMapper {
    List<UserRoleRelation> queryUserRoleList(Integer id);
    int insertBySelective(UserRole userRole);
    int deleteByPrimaryKey(int id);
    UserRole selectByUserIdRoleId(UserRole userRole);
}
