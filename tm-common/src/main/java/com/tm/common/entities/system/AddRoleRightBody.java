package com.tm.common.entities.system;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AddRoleRightBody {
    @NotNull(message = "角色id不能为空")
    private Integer roleId;
    @NotNull(message = "权限id列表不能为空")
    private List<Integer> rightIdList;
}
