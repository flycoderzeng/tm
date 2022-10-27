package com.tm.web.service;

import com.tm.common.base.mapper.CronJobPlanRelationMapper;
import com.tm.common.base.mapper.PlanCronJobMapper;
import com.tm.common.base.model.CronJobPlanRelation;
import com.tm.common.base.model.Menu;
import com.tm.common.base.model.PlanCronJob;
import com.tm.common.base.model.User;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.CommonTableQueryBody;
import com.tm.common.entities.base.CommonTableQueryResponse;
import com.tm.common.entities.common.enumerate.ResultCodeEnum;
import com.tm.web.utils.FindUtils;
import com.tm.common.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service(value = "planCronJobService")
public class PlanCronJobService extends BaseService {
    @Autowired
    private PlanCronJobMapper planCronJobMapper;
    @Autowired
    private CronJobPlanRelationMapper cronJobPlanRelationMapper;

    public int delete(Integer id) {
        return planCronJobMapper.deleteByPrimaryKey(id);
    }

    public PlanCronJob load(Integer id) {
        PlanCronJob planCronJob = planCronJobMapper.findById(id);
        if(planCronJob == null) {
            log.error("定时任务没有找到, {}", id);
            return null;
        }
        planCronJob.setPlanList(cronJobPlanRelationMapper.selectByCronJobId(planCronJob.getId()));
        return planCronJob;
    }

    public CommonTableQueryResponse queryList(CommonTableQueryBody body, User loginUser) {
        setQueryUserInfo(body, loginUser);
        CommonTableQueryResponse response = new CommonTableQueryResponse<Menu>();
        response.setRows(planCronJobMapper.queryList(body));
        response.setTotal(planCronJobMapper.countList(body));
        return response;
    }

    public int save(PlanCronJob body, User loginUser) {
        if(body.getId() == null || body.getId() < 1) {
            body.setAddUser(loginUser.getUsername());
            body.setAddTime(new Date());
            planCronJobMapper.insertBySelective(body);
        }else{
            body.setLastModifyUser(loginUser.getUsername());
            body.setLastModifyTime(new Date());
            planCronJobMapper.updateBySelective(body);
        }
        saveCronJobRelation(body);
        return body.getId();
    }

    private void saveCronJobRelation(PlanCronJob body) {
        List<CronJobPlanRelation> currentRelations = cronJobPlanRelationMapper.selectByCronJobId(body.getId());
        if(body.getPlanList() == null || body.getPlanList().isEmpty()) {
            // 删除已有的关联
            if(currentRelations != null && !currentRelations.isEmpty()) {
                for (CronJobPlanRelation cronJobPlanRelation : currentRelations) {
                    cronJobPlanRelationMapper.deleteByPrimaryKey(cronJobPlanRelation.getId());
                }
            }
        }else{
            if(currentRelations == null || currentRelations.isEmpty()) {
                for (CronJobPlanRelation cronJobPlanRelation : body.getPlanList()) {
                    cronJobPlanRelation.setPlanCronJobId(body.getId());
                    cronJobPlanRelationMapper.insertBySelective(cronJobPlanRelation);
                }
            }else{
                for (CronJobPlanRelation relation : currentRelations) {
                    relation.setStatus(1);
                }
                for (CronJobPlanRelation cronJobPlanRelation : body.getPlanList()) {
                    CronJobPlanRelation relation = FindUtils.find(cronJobPlanRelation, currentRelations);
                    if(relation == null) {
                        cronJobPlanRelation.setId(null);
                        cronJobPlanRelation.setPlanCronJobId(body.getId());
                        cronJobPlanRelationMapper.insertBySelective(cronJobPlanRelation);
                    }else{
                        relation.setPlanId(cronJobPlanRelation.getPlanId());
                        relation.setPlanCronJobId(body.getId());
                        relation.setEnvId(cronJobPlanRelation.getEnvId());
                        relation.setRunType(cronJobPlanRelation.getRunType());
                        relation.setStatus(0);
                    }
                }
                for (CronJobPlanRelation relation : currentRelations) {
                    cronJobPlanRelationMapper.updateBySelective(relation);
                }
            }
        }
    }

    public BaseResponse checkCronExpression(String expression) {
        List<String> runDates = new ArrayList<>();
        try {
            CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
            cronTriggerImpl.setCronExpression(expression);

            Calendar calendar = Calendar.getInstance();
            Date now = calendar.getTime();
            calendar.add(Calendar.MONTH, 3);

            // 这里的时间是根据corn表达式算出来的值
            List<Date> dates = TriggerUtils.computeFireTimesBetween(
                    cronTriggerImpl, null, now,
                    calendar.getTime());

            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            int i = 0;
            for (Date date : dates) {
                runDates.add(dateFormat.format(date));
                i++;
                if(i > 5) {
                    break;
                }
            }
        } catch (ParseException e) {
            log.error("expression invalid, ", e);
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR.getCode(), e.getMessage());
        }

        return ResultUtils.success(runDates);
    }
}
