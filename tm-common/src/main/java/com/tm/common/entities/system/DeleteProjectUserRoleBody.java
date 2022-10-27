package com.tm.common.entities.system;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeleteProjectUserRoleBody {
    @NotNull(message = "项目id不能为空")
    private Integer projectId;
    @NotNull(message = "用户id不能为空")
    private Integer userId;
    @NotNull(message = "角色id不能为空")
    private Integer roleId;
}
