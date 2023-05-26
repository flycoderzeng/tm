package com.tm.lc.models.testmanage;

import com.yahoo.elide.annotation.Include;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Table(name = "cron_job_plan_relation")
@Include(name="cron_job_plan_relation")
@Entity
@Getter
@Setter
public class CronJobPlanRelation {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "plan_id")
    private Integer planId;
    @Column(name = "env_id")
    private Integer envId;
    @Column(name = "run_type")
    private Integer runType;
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "plan_cron_job_id",referencedColumnName = "id")
    private PlanCronJob planCronJob;
}
