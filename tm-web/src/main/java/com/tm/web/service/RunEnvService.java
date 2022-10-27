package com.tm.web.service;

import com.tm.common.base.mapper.RunEnvMapper;
import com.tm.common.base.model.Menu;
import com.tm.common.base.model.RunEnv;
import com.tm.common.base.model.User;
import com.tm.common.entities.base.CommonTableQueryBody;
import com.tm.common.entities.base.CommonTableQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service(value = "runEnvService")
public class RunEnvService {
    @Autowired
    private RunEnvMapper runEnvMapper;

    public int delete(Integer id) {
        return runEnvMapper.deleteByPrimaryKey(id);
    }

    public RunEnv load(Integer id) {
        return runEnvMapper.findById(id);
    }

    public CommonTableQueryResponse queryList(CommonTableQueryBody body) {
        CommonTableQueryResponse response = new CommonTableQueryResponse<Menu>();
        response.setRows(runEnvMapper.queryList(body));
        response.setTotal(runEnvMapper.countList(body));
        return response;
    }

    public int save(RunEnv body, User loginUser) {
        if(body.getId() == null || body.getId() < 1) {
            body.setAddUser(loginUser.getAddUser());
            body.setAddTime(new Date());
            runEnvMapper.insertBySelective(body);
        }else{
            body.setLastModifyUser(loginUser.getLastModifyUser());
            body.setLastModifyTime(new Date());
            runEnvMapper.updateBySelective(body);
        }
        return body.getId();
    }

    public List<RunEnv> getAllRunEnv() {
        return runEnvMapper.getAllRunEnv();
    }
}
