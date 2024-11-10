package com.tm.worker.service;

import com.tm.common.base.mapper.CaseExecuteResultMapper;
import com.tm.common.base.mapper.CaseStepExecuteResultMapper;
import com.tm.common.base.mapper.CaseVariableValueResultMapper;
import com.tm.common.base.model.CaseExecuteResult;
import com.tm.common.base.model.CaseStepExecuteResult;
import com.tm.common.base.model.CaseVariableValueResult;
import com.tm.common.entities.autotest.CaseExecuteLogOperate;
import com.tm.worker.message.MessageSendReceiveService;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CaseResultLogService {
    @Getter
    @Value("${spring.autotest.result.case-result-split-table-type}")
    private Integer splitCaseResultTableType;
    @Getter
    @Value("${spring.autotest.result.variable-result-split-table-type}")
    private Integer splitVariableTableType;
    @Getter
    @Value("${spring.autotest.result.case-step-result-split-table-type}")
    private Integer splitCaseStepResultTableType;

    private final CaseExecuteResultMapper caseExecuteResultMapper;
    private final CaseVariableValueResultMapper caseVariableValueResultMapper;
    private final CaseStepExecuteResultMapper caseStepExecuteResultMapper;
    private final MessageSendReceiveService messageSendReceiveService;

    @Inject
    public CaseResultLogService(CaseExecuteResultMapper caseExecuteResultMapper,
                                CaseVariableValueResultMapper caseVariableValueResultMapper,
                                CaseStepExecuteResultMapper caseStepExecuteResultMapper,
                                MessageSendReceiveService messageSendReceiveService) {
        this.caseExecuteResultMapper = caseExecuteResultMapper;
        this.caseVariableValueResultMapper = caseVariableValueResultMapper;
        this.caseStepExecuteResultMapper = caseStepExecuteResultMapper;
        this.messageSendReceiveService = messageSendReceiveService;
    }

    public void put(CaseExecuteLogOperate logOperate) {
        messageSendReceiveService.sendTestResultLog(logOperate);
    }

    public int insert(Object logRowObject) {
        int i = 0;
        if (logRowObject instanceof CaseExecuteResult logRow) {
            i = caseExecuteResultMapper.insertBySelective(logRow);
        } else if (logRowObject instanceof CaseVariableValueResult logRow) {
            i = caseVariableValueResultMapper.insertBySelective(logRow);
        } else if (logRowObject instanceof CaseStepExecuteResult logRow) {
            i = caseStepExecuteResultMapper.insertBySelective(logRow);
        }
        return i;
    }

    public int update(Object logRowObject) {
        int i = 0;
        if(logRowObject instanceof CaseExecuteResult logRow) {
            i = caseExecuteResultMapper.updateBySelective(logRow);
        }else if(logRowObject instanceof CaseStepExecuteResult logRow) {
            i = caseStepExecuteResultMapper.updateBySelective(logRow);
        }
        return i;
    }

}
