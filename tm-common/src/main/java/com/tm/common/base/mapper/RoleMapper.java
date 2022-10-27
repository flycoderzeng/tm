package com.tm.common.base.mapper;

import com.tm.common.base.model.Role;
import com.tm.common.entities.base.CommonTableQueryBody;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    List<Role> queryList(CommonTableQueryBody body);
    int countList(CommonTableQueryBody body);
    Role findById(Integer id);
    List<Role> findByType(Integer id);
    int updateBySelective(Role role);
    int insertBySelective(Role role);
    int deleteByPrimaryKey(int id);

    List<Role> getRightRelatedRoles(@Param("rightId") Integer rightId);

    List<Role> getUserRoles(@Param("userId") Integer userId);
}
