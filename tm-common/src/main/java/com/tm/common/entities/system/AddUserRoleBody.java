package com.tm.common.entities.system;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AddUserRoleBody {
    @NotNull(message = "用户id不能为空")
    private Integer userId;
    @NotNull(message = "角色id列表不能为空")
    private List<Integer> roleIdList;
}
