package com.tm.web.service;

import com.tm.common.base.mapper.ApiIpPortConfigMapper;
import com.tm.common.base.model.*;
import com.tm.common.entities.base.CommonTableQueryBody;
import com.tm.common.entities.base.CommonTableQueryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service(value = "apiIpPortConfigService")
public class ApiIpPortConfigService extends BaseService {
    @Autowired
    private ApiIpPortConfigMapper apiIpPortConfigMapper;

    public int delete(Integer id) {
        return apiIpPortConfigMapper.deleteByPrimaryKey(id);
    }

    public ApiIpPortConfig load(Integer id) {
        ApiIpPortConfig ipPortConfig = apiIpPortConfigMapper.findById(id);
        if(ipPortConfig == null) {
            log.error("url配置没有找到, {}", id);
            return null;
        }
        return ipPortConfig;
    }

    public CommonTableQueryResponse queryList(CommonTableQueryBody body, User loginUser) {
        setQueryUserInfo(body, loginUser);
        CommonTableQueryResponse response = new CommonTableQueryResponse<Menu>();
        response.setRows(apiIpPortConfigMapper.queryList(body));
        response.setTotal(apiIpPortConfigMapper.countList(body));
        return response;
    }

    public int save(ApiIpPortConfig body, User loginUser) {
        if(body.getId() == null || body.getId() < 1) {
            body.setAddUser(loginUser.getUsername());
            body.setAddTime(new Date());
            apiIpPortConfigMapper.insertBySelective(body);
        }else{
            body.setLastModifyUser(loginUser.getUsername());
            body.setLastModifyTime(new Date());
            apiIpPortConfigMapper.updateBySelective(body);
        }
        return body.getId();
    }
}
