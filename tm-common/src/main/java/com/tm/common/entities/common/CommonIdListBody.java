package com.tm.common.entities.common;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CommonIdListBody {
    @NotNull(message = "id列表不能为空")
    private List<Integer> idList;
}
