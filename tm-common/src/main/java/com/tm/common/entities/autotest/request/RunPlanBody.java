package com.tm.common.entities.autotest.request;

import com.tm.common.entities.common.KeyValueRow;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RunPlanBody {
    @NotNull(message = "计划id不能为空")
    private Integer planId;
    // 1-非组合 2-组合
    @NotNull(message = "运行类型不能为空")
    private Integer runType;
    private Integer runEnvId;
    //1-计划 2-用例 3-定时计划
    private Integer fromType = 1;
    private Integer planCronJobId;
    @Min(0)
    private Integer priority = 0;

    private List<KeyValueRow> planVariables;

    private String runDescription;
}
