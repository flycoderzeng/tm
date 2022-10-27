package com.tm.common.entities.system;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;


@Data
public class AddUserToProjectBody {
    @NotNull(message = "项目 id 不能为空")
    private Integer projectId;
    @NotNull(message = "user id 不能为空")
    private List<Integer> userIdList;
    private List<Integer> roleIdList;
}
