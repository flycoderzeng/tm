package com.tm.web.cronjob;


import com.tm.common.base.mapper.CaseStepExecuteResultMapper;
import com.tm.common.utils.TableSuffixUtils;
import jakarta.inject.Inject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CreateCaseStepExecuteTableCronJob {
    @Value("${spring.autotest.result.case-step-result-split-table-type}")
    private Integer splitTableType;

    private final CaseStepExecuteResultMapper caseStepExecuteResultMapper;

    @Inject
    public CreateCaseStepExecuteTableCronJob(CaseStepExecuteResultMapper caseStepExecuteResultMapper) {
        this.caseStepExecuteResultMapper = caseStepExecuteResultMapper;
    }

    @Scheduled(cron = "0 */1 * * * ?")
    public void createCaseStepExecuteResultTable() {
        Date currDate = new Date();
        for(int i = 0; i < 2; i++) {
            String suffix = TableSuffixUtils.getTableSuffix(currDate, splitTableType, i);
            caseStepExecuteResultMapper.createCaseStepExecuteResultTable(suffix);
        }
    }
}
