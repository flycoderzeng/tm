package com.tm.web.cronjob;

import com.tm.common.base.mapper.PlanCronJobMapper;
import com.tm.common.base.model.PlanCronJob;
import com.tm.web.planjob.PlanQuartzJobManager;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronTrigger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PlanCronJobRefresh {
    private final PlanQuartzJobManager planQuartzJobManager;
    private final PlanCronJobMapper planCronJobMapper;

    @Inject
    public PlanCronJobRefresh(PlanQuartzJobManager planQuartzJobManager, PlanCronJobMapper planCronJobMapper) {
        this.planQuartzJobManager = planQuartzJobManager;
        this.planCronJobMapper = planCronJobMapper;
    }

    @Scheduled(cron = "0 */5 * * * ?")
    public void refreshPlanCronJob() {
        log.info("刷新定时计划任务");
        final List<PlanCronJob> allPlanCronJobs = planCronJobMapper.getAllPlanCronJobs();
        for (PlanCronJob cronJob : allPlanCronJobs) {
            if(cronJob.getStatus() == null || cronJob.getStatus() != 0) {
                log.info("删除已经被删除的定时任务");
                try {
                    planQuartzJobManager.deleteScheduleJob(cronJob.getId());
                } catch (Exception e) {
                    log.error("{}", e);
                }
            }else{
                //检查cron表达式是否有变化
                final CronTrigger cronTrigger = planQuartzJobManager.getCronTrigger(planQuartzJobManager.getScheduler(), cronJob.getId());
                if(cronTrigger == null) {
                    log.info("有新的定时任务创建, 创建定时任务id： {}", cronJob.getId());
                    try {
                        planQuartzJobManager.createScheduleJob(cronJob);
                    } catch (Exception e) {
                        log.error("{}", e);
                    }
                } else if (!StringUtils.equals(cronTrigger.getCronExpression(), cronJob.getCronExpression())) {
                    log.info("定时任务id： {}， cron表达式有变化，重新创建定时任务", cronJob.getId());
                    try {
                        planQuartzJobManager.deleteScheduleJob(cronJob.getId());
                        planQuartzJobManager.createScheduleJob(cronJob);
                    } catch (Exception e) {
                        log.error("{}", e);
                    }
                }
            }
        }
    }
}
