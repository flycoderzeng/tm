package com.tm.common.base.mapper;

import com.tm.common.base.model.RoleRight;
import com.tm.common.base.model.RoleRightRelation;

import java.util.List;

public interface RoleRightMapper {
    List<RoleRightRelation> queryRoleRightList(Integer id);
    int insertBySelective(RoleRight roleRight);
    int deleteByPrimaryKey(int id);
    RoleRight selectByRoleIdRightId(RoleRight roleRight);
}
