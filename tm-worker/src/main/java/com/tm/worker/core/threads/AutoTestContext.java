package com.tm.worker.core.threads;

import com.tm.worker.core.cookie.AutoTestCookie;
import com.tm.worker.core.task.TaskService;
import com.tm.worker.core.task.CaseTask;
import com.tm.worker.core.task.PlanTask;
import com.tm.worker.core.variable.AutoTestVariables;
import lombok.Data;

@Data
public class AutoTestContext {
    private AutoTestVariables caseVariables;

    private AutoTestCookie autoTestCookie;
    private CaseTask caseTask;
    private PlanTask planTask;
    private TaskService taskService;

    public AutoTestContext() {}

    public AutoTestContext(CaseTask caseTask, TaskService taskService) {
        this.caseTask = caseTask;
        this.taskService = taskService;
        this.planTask = caseTask.getPlanTask();
    }
}
