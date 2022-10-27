package com.tm.common.entities.autotest.request;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GetNodesTreeBody {
    @NotNull(message = "项目id不能为空")
    private Integer projectId;
    @NotNull(message = "父id不能为空")
    private Integer parentId;
    @NotNull(message = "数据类型id不能为空")
    private Integer dataTypeId;
    private Integer level;
}
