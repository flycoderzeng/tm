package com.tm.lc.models.testmanage;

import com.tm.lc.convert.DateAndString;
import com.tm.lc.hooks.EntityPublicCreateHook;
import com.tm.lc.hooks.EntityPublicModifyHook;
import com.tm.lc.models.CommonSixItemsElideModel;
import com.yahoo.elide.annotation.DeletePermission;
import com.yahoo.elide.annotation.Include;
import com.yahoo.elide.annotation.LifeCycleHookBinding;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

import static com.yahoo.elide.annotation.LifeCycleHookBinding.Operation.CREATE;
import static com.yahoo.elide.annotation.LifeCycleHookBinding.Operation.UPDATE;
import static com.yahoo.elide.annotation.LifeCycleHookBinding.TransactionPhase.PRECOMMIT;

@Table(name = "plan_cron_jobs")
@Include(name="plan_cron_job")
@Entity
@Getter
@Setter
@LifeCycleHookBinding(operation = CREATE, phase = PRECOMMIT, hook = EntityPublicCreateHook.class)
@LifeCycleHookBinding(operation = UPDATE, phase = PRECOMMIT, hook = EntityPublicModifyHook.class)
@DeletePermission(expression = "user is a root admin")
public class PlanCronJob extends CommonSixItemsElideModel {
    private String name;

    @Lob
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "cron_expression")
    private String cronExpression;

    @Column(name = "last_run_time", columnDefinition = "TIMESTAMP")
    @Convert(converter = DateAndString.class)
    protected String lastRunTime;

    @OneToMany
    @JoinColumn(name = "plan_cron_job_id", referencedColumnName = "id")
    private Set<CronJobPlanRelation> cronJobPlanRelations;
}
