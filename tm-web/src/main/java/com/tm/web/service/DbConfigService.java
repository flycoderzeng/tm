package com.tm.web.service;

import com.tm.common.base.mapper.DbConfigMapper;
import com.tm.common.base.model.DbConfig;
import com.tm.common.base.model.Menu;
import com.tm.common.base.model.User;
import com.tm.common.entities.base.CommonTableQueryBody;
import com.tm.common.entities.base.CommonTableQueryResponse;
import com.tm.web.utils.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service(value = "dbConfigService")
public class DbConfigService extends BaseService {
    @Value("${rsa.publicKey}")
    private String publicKeyStr;

    @Value("${rsa.privateKey}")
    private String privateKeyStr;

    @Autowired
    private DbConfigMapper dbConfigMapper;

    public int delete(Integer id) {
        return dbConfigMapper.deleteByPrimaryKey(id);
    }

    public DbConfig load(Integer id) {
        DbConfig dbConfig = dbConfigMapper.findById(id);
        if(dbConfig == null) {
            log.error("db配置没有找到, {}", id);
            return null;
        }
        decrypt(dbConfig);
        return dbConfig;
    }

    public CommonTableQueryResponse queryList(CommonTableQueryBody body, User loginUser) {
        setQueryUserInfo(body, loginUser);
        CommonTableQueryResponse response = new CommonTableQueryResponse<Menu>();
        List<DbConfig> dbConfigs = dbConfigMapper.queryList(body);
        response.setRows(dbConfigs);
        response.setTotal(dbConfigMapper.countList(body));
        if(dbConfigs != null && !dbConfigs.isEmpty()) {
            for (DbConfig dbConfig : dbConfigs) {
                dbConfig.setPassword("");
                decrypt(dbConfig);
            }
        }
        return response;
    }

    public int save(DbConfig body, User loginUser) {
        encrypt(body);
        if(body.getId() == null || body.getId() < 1) {
            body.setAddUser(loginUser.getUsername());
            body.setAddTime(new Date());
            dbConfigMapper.insertBySelective(body);
        }else{
            body.setLastModifyUser(loginUser.getUsername());
            body.setLastModifyTime(new Date());
            dbConfigMapper.updateBySelective(body);
        }
        return body.getId();
    }

    public void encrypt(DbConfig body) {
        if(StringUtils.isNotBlank(body.getUsername())) {
            body.setUsername(RSAUtils.encrypt(publicKeyStr, body.getUsername()));
        }
        if(StringUtils.isNotBlank(body.getPassword())) {
            body.setPassword(RSAUtils.encrypt(publicKeyStr, body.getPassword()));
        }
    }

    public void decrypt(DbConfig body) {
        if(StringUtils.isNotBlank(body.getUsername())) {
            body.setUsername(RSAUtils.decrypt(privateKeyStr, body.getUsername()));
        }
        if(StringUtils.isNotBlank(body.getPassword())) {
            body.setPassword(RSAUtils.decrypt(privateKeyStr, body.getPassword()));
        }
    }
}
