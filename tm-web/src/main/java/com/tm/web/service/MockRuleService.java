package com.tm.web.service;

import com.tm.common.base.mapper.HttpMockRuleMapper;
import com.tm.common.base.model.HttpMockRule;
import com.tm.common.base.model.User;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.CommonTableQueryBody;
import com.tm.common.entities.base.CommonTableQueryResponse;
import com.tm.common.entities.common.enumerate.ResultCodeEnum;
import com.tm.common.utils.ResultUtils;
import jakarta.inject.Inject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MockRuleService {
    private final HttpMockRuleMapper httpMockRuleMapper;

    @Inject
    public MockRuleService(HttpMockRuleMapper httpMockRuleMapper) {
        this.httpMockRuleMapper = httpMockRuleMapper;
    }

    public BaseResponse saveHttpMockRule(HttpMockRule rule, User loginUser) {
        if(rule.getId() == null || rule.getId() < 1) {
            rule.setAddTime(new Date());
            rule.setAddUser(loginUser.getUsername());
            httpMockRuleMapper.insertBySelective(rule);
        }else{
            rule.setLastModifyTime(new Date());
            rule.setLastModifyUser(loginUser.getUsername());
            httpMockRuleMapper.updateBySelective(rule);
        }
        return ResultUtils.success(rule.getId());
    }

    public BaseResponse deleteHttpMockRule(Integer id, User loginUser) {
        HttpMockRule rule = httpMockRuleMapper.selectByPrimaryId(id);
        if(rule == null) {
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
        rule.setLastModifyTime(new Date());
        rule.setLastModifyUser(loginUser.getUsername());
        rule.setStatus(1);
        httpMockRuleMapper.updateBySelective(rule);
        return ResultUtils.success();
    }

    public BaseResponse queryHttpMockRuleList(CommonTableQueryBody body) {
        List<HttpMockRule> rules = new ArrayList<>();
        final int total = httpMockRuleMapper.countList(body);
        if(total > 0) {
            rules = httpMockRuleMapper.queryList(body);
        }
        return ResultUtils.success(new CommonTableQueryResponse<>(rules, total));
    }

    public BaseResponse loadHttpMockRule(Integer id) {
        return ResultUtils.success(httpMockRuleMapper.selectByPrimaryId(id));
    }
}
