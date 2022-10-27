package com.tm.web.cronjob;


import com.tm.common.base.mapper.CaseExecuteResultMapper;
import com.tm.common.utils.TableSuffixUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CreateCaseExecuteTableCronJob {
    @Value("${spring.autotest.result.case-result-split-table-type}")
    private Integer splitTableType;

    @Autowired
    private CaseExecuteResultMapper caseExecuteResultMapper;

    @Scheduled(cron = "0 */1 * * * ?")
    public void createCaseExecuteResultTable() {
        Date currDate = new Date();
        for(int i = 0; i < 2; i++) {
            String suffix = TableSuffixUtils.getTableSuffix(currDate, splitTableType, i);
            caseExecuteResultMapper.createCaseExecuteResultTable(suffix);
        }
    }
}
