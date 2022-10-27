package com.tm.common.base.model;

import com.tm.common.entities.autotest.ParameterDefineRow;
import lombok.Data;

import java.util.List;

@Data
public class PlatformApi extends CommonNameDescriptionModel {
    private Integer id;
    private String defineJson = "";
    private List<ParameterDefineRow> rows;
}
