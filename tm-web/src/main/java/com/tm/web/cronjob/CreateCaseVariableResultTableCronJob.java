package com.tm.web.cronjob;


import com.tm.common.base.mapper.CaseVariableValueResultMapper;
import com.tm.common.utils.TableSuffixUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CreateCaseVariableResultTableCronJob {
    @Value("${spring.autotest.result.variable-result-split-table-type}")
    private Integer splitTableType;

    @Autowired
    private CaseVariableValueResultMapper caseVariableValueResultMapper;

    @Scheduled(cron = "0 */1 * * * ?")
    public void createCaseVariableResultTable() {
        Date currDate = new Date();
        for(int i = 0; i < 2; i++) {
            String suffix = TableSuffixUtils.getTableSuffix(currDate, splitTableType, i);
            caseVariableValueResultMapper.createCaseVariableValueResultTable(suffix);
        }
    }
}
