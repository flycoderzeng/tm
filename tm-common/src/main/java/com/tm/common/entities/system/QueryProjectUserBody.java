package com.tm.common.entities.system;

import com.tm.common.entities.base.CommonTableQueryBody;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Data
public class QueryProjectUserBody extends CommonTableQueryBody {
    @NotNull(message = "项目id不能为空")
    @Min(1)
    private Integer projectId;
}
