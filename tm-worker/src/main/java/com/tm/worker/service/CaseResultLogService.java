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

import java.util.concurrent.ArrayBlockingQueue;
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


    private ConcurrentLinkedDeque queue = new ConcurrentLinkedDeque<CaseExecuteLogOperate>();

    public void put(CaseExecuteLogOperate logOperate) {
        boolean add = queue.add(logOperate);
        if(!add) {
            log.error("往日志队列里面插入日志失败");
        }
    }

    public ConcurrentLinkedDeque<CaseExecuteLogOperate> getLogQueue() {
        return queue;
    }

    public void insert(Object logRow) {
        if(logRow instanceof CaseExecuteResult) {
            int i = caseExecuteResultMapper.insertBySelective((CaseExecuteResult) logRow);
            log.debug("插入用例执行结果，影响了{}条记录", i);
        }else if(logRow instanceof CaseVariableValueResult) {
            int i = caseVariableValueResultMapper.insertBySelective((CaseVariableValueResult) logRow);
            log.debug("插入用例变量结果，影响了{}条记录", i);
        }else if(logRow instanceof CaseStepExecuteResult) {
            int i = caseStepExecuteResultMapper.insertBySelective((CaseStepExecuteResult) logRow);
            log.debug("插入用例步骤执行结果，影响了{}条记录", i);
        }
    }

    public void update(Object logRow) {
        if(logRow instanceof CaseExecuteResult) {
            int i = caseExecuteResultMapper.updateBySelective((CaseExecuteResult) logRow);
            log.debug("更新用例执行结果，影响了{}条记录", i);
        }else if(logRow instanceof CaseStepExecuteResult) {
            int i = caseStepExecuteResultMapper.updateBySelective((CaseStepExecuteResult) logRow);
            log.debug("更新用例步骤执行结果，影响了{}条记录", i);

        }
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
