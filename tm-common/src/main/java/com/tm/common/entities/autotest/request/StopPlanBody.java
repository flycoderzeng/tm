package com.tm.common.entities.autotest.request;

import com.tm.common.base.model.User;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class StopPlanBody implements Serializable {
    @NotNull(message = "计划结果id不能为空")
    private Integer planResultId;

    private User user;
}
