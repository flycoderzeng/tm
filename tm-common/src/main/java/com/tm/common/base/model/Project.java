package com.tm.common.base.model;

import com.tm.common.entities.testmanage.ProjectStatisticsInfo;
import lombok.Data;


@Data
public class Project extends Common6ItemsModel {
    private String name;
    private String description;
    private ProjectStatisticsInfo projectStatisticsInfo;
}
