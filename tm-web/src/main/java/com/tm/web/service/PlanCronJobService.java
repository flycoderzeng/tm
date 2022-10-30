package com.tm.web.service;


import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.common.enumerate.ResultCodeEnum;
import com.tm.common.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;
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
