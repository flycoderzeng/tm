package com.tm.common.base.mapper;

import com.tm.common.base.model.Role;
import com.tm.common.entities.base.CommonTableQueryBody;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {

    List<Role> getRightRelatedRoles(@Param("rightId") Integer rightId);

    List<Role> getUserRoles(@Param("userId") Integer userId);
}
