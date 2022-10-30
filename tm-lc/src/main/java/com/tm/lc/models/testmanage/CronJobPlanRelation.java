package com.tm.lc.models.testmanage;

import com.yahoo.elide.annotation.Include;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "cron_job_plan_relation")
@Include(name="cron_job_plan_relation")
@Entity
@Getter
@Setter
public class CronJobPlanRelation {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private Integer planId;
    private Integer envId;
    private Integer runType;
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "plan_cron_job_id",referencedColumnName = "id")
    private PlanCronJob planCronJob;
}
