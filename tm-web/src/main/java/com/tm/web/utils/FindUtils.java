package com.tm.web.utils;

import com.tm.common.base.model.CronJobPlanRelation;

import java.util.List;

public class FindUtils {
    public static<T> T find(T dest, List<T> list) {
        if(dest instanceof CronJobPlanRelation jobPlanRelation) {
            for (Object o : list) {
                CronJobPlanRelation relation = (CronJobPlanRelation)o;
                if(relation.getId().equals(jobPlanRelation.getId())) {
                    return (T) relation;
                }
                if(relation.getPlanId().equals(jobPlanRelation.getPlanId())
                        && relation.getEnvId().equals(jobPlanRelation.getEnvId())
                && relation.getRunType().equals(jobPlanRelation.getRunType()) &&
                relation.getPlanCronJobId().equals(jobPlanRelation.getPlanCronJobId())) {
                    return (T) relation;
                }
            }
        }
        return null;
    }
}
