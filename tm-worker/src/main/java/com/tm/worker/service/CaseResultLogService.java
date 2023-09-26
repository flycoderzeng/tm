package com.tm.worker.service;

import com.tm.common.base.mapper.CaseExecuteResultMapper;
import com.tm.common.base.mapper.CaseStepExecuteResultMapper;
import com.tm.common.base.mapper.CaseVariableValueResultMapper;
import com.tm.common.base.model.CaseExecuteResult;
import com.tm.common.base.model.CaseStepExecuteResult;
import com.tm.common.base.model.CaseVariableValueResult;
import com.tm.common.entities.autotest.CaseExecuteLogOperate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

@Slf4j
@Component
public class CaseResultLogService {
    @Value("${spring.autotest.result.case-result-split-table-type}")
    private Integer splitCaseResultTableType;
    @Value("${spring.autotest.result.variable-result-split-table-type}")
    private Integer splitVariableTableType;
    @Value("${spring.autotest.result.case-step-result-split-table-type}")
    private Integer splitCaseStepResultTableType;

    @Autowired
    private CaseExecuteResultMapper caseExecuteResultMapper;
    @Autowired
    private CaseVariableValueResultMapper caseVariableValueResultMapper;
    @Autowired
    private CaseStepExecuteResultMapper caseStepExecuteResultMapper;


    private Deque<CaseExecuteLogOperate> queue = new ConcurrentLinkedDeque<>();

    public void put(CaseExecuteLogOperate logOperate) {
        boolean add = queue.add(logOperate);
        if(!add) {
            log.error("往日志队列里面插入日志失败");
        }
    }

    public Deque<CaseExecuteLogOperate> getLogQueue() {
        return queue;
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

    public Integer getSplitCaseResultTableType() {
        return this.splitCaseResultTableType;
    }

    public Integer getSplitVariableTableType() {
        return this.splitVariableTableType;
    }

    public Integer getSplitCaseStepResultTableType() {
        return this.splitCaseStepResultTableType;
    }
}
