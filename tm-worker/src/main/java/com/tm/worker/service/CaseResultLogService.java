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

    public void insert(Object logRowObject) {
        if(logRowObject instanceof CaseExecuteResult logRow) {
            int i = caseExecuteResultMapper.insertBySelective(logRow);
            log.debug("插入用例执行结果，影响了{}条记录", i);
        }else if(logRowObject instanceof CaseVariableValueResult logRow) {
            int i = caseVariableValueResultMapper.insertBySelective(logRow);
            log.debug("插入用例变量结果，影响了{}条记录", i);
        }else if(logRowObject instanceof CaseStepExecuteResult logRow) {
            int i = caseStepExecuteResultMapper.insertBySelective(logRow);
            log.debug("插入用例步骤执行结果，影响了{}条记录", i);
        }
    }

    public void update(Object logRowObject) {
        if(logRowObject instanceof CaseExecuteResult logRow) {
            int i = caseExecuteResultMapper.updateBySelective(logRow);
            log.debug("更新用例执行结果，影响了{}条记录", i);
        }else if(logRowObject instanceof CaseStepExecuteResult logRow) {
            int i = caseStepExecuteResultMapper.updateBySelective(logRow);
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
