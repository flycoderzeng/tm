package com.tm.web.planjob;

import com.tm.common.base.mapper.PlanCronJobMapper;
import com.tm.common.base.model.PlanCronJob;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Order(value = 10000)
public class PlanQuartzJobInitializer implements ApplicationRunner {
    private final PlanQuartzJobManager planQuartzJobManager;
    private final PlanCronJobMapper planCronJobMapper;

    @Inject
    public PlanQuartzJobInitializer(PlanQuartzJobManager planQuartzJobManager,
                                    PlanCronJobMapper planCronJobMapper) {
        this.planQuartzJobManager = planQuartzJobManager;
        this.planCronJobMapper = planCronJobMapper;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("计划定时任务初始化");
        final List<PlanCronJob> allPlanCronJobs = planCronJobMapper.getAllPlanCronJobs();
        for (PlanCronJob planCronJob : allPlanCronJobs) {
            planQuartzJobManager.createScheduleJob(planCronJob);
        }
        log.info("计划定时任务初始化完成");
    }
}
