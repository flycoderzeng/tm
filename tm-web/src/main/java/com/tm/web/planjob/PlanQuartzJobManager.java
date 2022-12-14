package com.tm.web.planjob;

import com.tm.common.base.model.PlanCronJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PlanQuartzJobManager {
    private final String JOB_NAME = "PLAN_JOB_";

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    public Scheduler getScheduler(){
        return schedulerFactoryBean.getScheduler();
    }

    public JobKey getJobKey(Integer jobId){
        return JobKey.jobKey(JOB_NAME + jobId);
    }

    public TriggerKey getTriggerKey(Integer jobId){
        return TriggerKey.triggerKey(JOB_NAME + jobId);
    }

    public CronTrigger getCronTrigger(Scheduler scheduler, Integer jobId){
        try {
            return (CronTrigger) scheduler.getTrigger(getTriggerKey(jobId));
        } catch (SchedulerException e) {
            throw new RuntimeException("获取定时任务CronTrigger出现异常", e);
        }
    }

    public void createScheduleJob(PlanCronJob planCronJob) {
        try {
            // 1.获取调度器 Scheduler
            Scheduler scheduler = getScheduler();
            // 2.定义 jobDetail(构建job信息)
            JobDetail jobDetail = JobBuilder.newJob(PlanQuartzJob.class).withIdentity(getJobKey(planCronJob.getId())).build();
            // 3.定义trigger（按新的cronExpression表达式构建一个新的trigger）
            // 不触发立即执行，等待下次Cron触发频率到达时刻开始按照Cron频率依次执行  withMisfireHandlingInstructionDoNothing
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(planCronJob.getCronExpression()).withMisfireHandlingInstructionDoNothing();
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(planCronJob.getId())).withSchedule(cronScheduleBuilder).build();
            // 放入参数，运行时的方法可以获取
            jobDetail.getJobDataMap().put(PlanQuartzJob.PLAN_QUARTZ_PARAM_KEY, planCronJob);
            // 4.执行
            scheduler.scheduleJob(jobDetail, trigger);
            log.info("创建计划定时任务，id: {}", planCronJob.getId());
        } catch (SchedulerException e) {
            throw new RuntimeException("创建定时任务失败",e);
        }
    }

    public void run(PlanCronJob planCronJob) {
        try {
            // 参数
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(PlanQuartzJob.PLAN_QUARTZ_PARAM_KEY, planCronJob);
            getScheduler().triggerJob(getJobKey(planCronJob.getId()), dataMap);
        } catch (SchedulerException e) {
            throw new RuntimeException("立即执行定时任务失败",e);
        }
    }

    public void deleteScheduleJob(Integer jobId) {
        // 1.获取调度器 Scheduler
        Scheduler scheduler = getScheduler();
        try {
            // 2.暂停触发器
            scheduler.pauseTrigger(getTriggerKey(jobId));
            // 3.删除触发器
            scheduler.unscheduleJob(getTriggerKey(jobId));
            // 4.删除任务
            scheduler.deleteJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new RuntimeException("删除定时任务失败",e);
        }
    }
}
