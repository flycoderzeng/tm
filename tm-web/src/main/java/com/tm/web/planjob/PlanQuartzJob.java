package com.tm.web.planjob;

import com.tm.common.base.mapper.CronJobPlanRelationMapper;
import com.tm.common.base.mapper.PlanCronJobMapper;
import com.tm.common.base.model.CronJobPlanRelation;
import com.tm.common.base.model.PlanCronJob;
import com.tm.common.entities.autotest.enumerate.PlanRunFromTypeEnum;
import com.tm.common.entities.autotest.request.RunPlanBody;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.lock.DistributedLockMysql;
import com.tm.web.service.AutoTestFeignService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.List;

@Slf4j
public class PlanQuartzJob extends QuartzJobBean {
    public static final String PLAN_QUARTZ_PARAM_KEY = "PLAN_QUARTZ_PARAM_KEY";
    public static final String PLAN_CRON_JOB_LOCK = "PLAN_CRON_JOB_LOCK";

    @Autowired
    private DistributedLockMysql distributedLockMysql;
    @Autowired
    private PlanCronJobMapper planCronJobMapper;
    @Autowired
    private AutoTestFeignService autoTestFeignService;
    @Autowired
    private CronJobPlanRelationMapper cronJobPlanRelationMapper;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        PlanCronJob planCronJob = (PlanCronJob) context.getMergedJobDataMap().get(PLAN_QUARTZ_PARAM_KEY);
        final Integer jobId = planCronJob.getId();
        log.info("执行计划定时任务, id: {}", jobId);
        planCronJob = planCronJobMapper.selectByPrimaryKey(jobId);
        if(planCronJob == null) {
            log.info("计划定时任务, id: {} 不存在", jobId);
            return;
        }
        if(planCronJob.getStatus() == null || planCronJob.getStatus() > 0) {
            log.info("计划定时任务, id: {} 已经被删除", jobId);
            return;
        }

        final List<CronJobPlanRelation> cronJobPlanRelations =
                cronJobPlanRelationMapper.selectByCronJobId(planCronJob.getId());
        planCronJob.setPlanList(cronJobPlanRelations);

        final boolean lock = distributedLockMysql.lock(jobId, PLAN_CRON_JOB_LOCK);
        if(lock) {
            log.info("执行定时任务中配置的计划");
            planCronJob.setLastRunTime(new Date());
            planCronJobMapper.updateBySelective(planCronJob);
            try {
                final List<CronJobPlanRelation> planList = planCronJob.getPlanList();
                if(planList != null && !planList.isEmpty()) {
                    for (CronJobPlanRelation cronJobPlanRelation : planList) {
                        final RunPlanBody runPlanBody = new RunPlanBody();
                        runPlanBody.setPlanId(cronJobPlanRelation.getPlanId());
                        runPlanBody.setRunType(cronJobPlanRelation.getRunType());
                        runPlanBody.setRunEnvId(cronJobPlanRelation.getEnvId());
                        runPlanBody.setFromType(PlanRunFromTypeEnum.CRON_JOB.value());
                        runPlanBody.setPlanCronJobId(jobId);
                        log.info("{}", runPlanBody);
                        final BaseResponse baseResponse = autoTestFeignService.runPlan(runPlanBody);
                        log.info("{}", baseResponse);
                    }
                }
                log.info("执行定时任务中配置的计划完成");
            } finally {
                distributedLockMysql.unlock(jobId, PLAN_CRON_JOB_LOCK);
            }
        } else {
            log.error("计划定时任务, id: {} 加锁失败", jobId);
        }
    }
}
