package com.tm.common.entities.autotest.request;

import com.tm.common.entities.base.IdBody;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;


@Data
public class SaveNodeBody extends IdBody {
    private Integer prevId;

    @NotNull(message = "项目id不能为空")
    private Integer projectId;

    @NotNull(message = "数据类型id不能为空")
    private Integer dataTypeId;

    private Integer parentId;

    @Range(min = 0,max = 1)
    private Integer isFolder;

    private String name;
    private String description;

    private Integer copyId;
}
