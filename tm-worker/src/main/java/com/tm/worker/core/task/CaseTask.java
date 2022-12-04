package com.tm.worker.core.task;

import com.tm.common.base.model.AutoCase;
import com.tm.worker.core.variable.AutoTestVariables;
import lombok.Data;


@Data
public class CaseTask {
    private String taskId;
    private PlanTask planTask;
    private AutoCase autoCase;
    private AutoTestVariables groupVariables;
    private Integer groupNo = 0;
    private String groupName;
    private Integer seq = 1;

    public CaseTask(String taskId, PlanTask planTask, AutoCase autoCase) {
        init1(taskId, planTask, autoCase);
    }

    public CaseTask(String taskId, PlanTask planTask, AutoCase autoCase, AutoTestVariables groupVariables) {
        init1(taskId, planTask, autoCase);
        this.groupVariables = groupVariables;
    }

    private void init1(String taskId, PlanTask planTask, AutoCase autoCase) {
        this.taskId = taskId;
        this.planTask = planTask;
        this.autoCase = autoCase;
    }
}
