package com.tm.web.planjob;

import com.tm.common.base.mapper.CronJobPlanRelationMapper;
import com.tm.common.base.mapper.PlanCronJobMapper;
import com.tm.common.base.model.CronJobPlanRelation;
import com.tm.common.base.model.PlanCronJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Order(value = 10000)
public class PlanQuartzJobInitializer implements ApplicationRunner {
    @Autowired
    private PlanQuartzJobManager planQuartzJobManager;

    @Autowired
    private PlanCronJobMapper planCronJobMapper;
    @Autowired
    private CronJobPlanRelationMapper cronJobPlanRelationMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("计划定时任务初始化");
        final List<PlanCronJob> allPlanCronJobs = planCronJobMapper.getAllPlanCronJobs();
        for (PlanCronJob planCronJob : allPlanCronJobs) {
            final List<CronJobPlanRelation> cronJobPlanRelations =
                    cronJobPlanRelationMapper.selectByCronJobId(planCronJob.getId());
            planCronJob.setPlanList(cronJobPlanRelations);
        }

        for (PlanCronJob planCronJob : allPlanCronJobs) {
            planQuartzJobManager.createScheduleJob(planCronJob);
        }
        log.info("计划定时任务初始化完成");
    }
}
