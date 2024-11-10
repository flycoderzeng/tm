package com.tm.web.service;

import com.tm.common.base.mapper.DataDictMapper;
import com.tm.common.base.mapper.HttpApiMapper;
import com.tm.common.base.model.DataDictModel;
import com.tm.common.base.model.HttpApi;
import com.tm.common.base.model.User;
import com.tm.common.entities.autotest.request.SaveNodeBody;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.common.enumerate.DataTypeEnum;
import com.tm.common.utils.ResultUtils;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("httpApiService")
public class HttpApiService extends BaseService {
    private final DataDictMapper dataDictMapper;
    private final HttpApiMapper httpApiMapper;

    @Inject
    public HttpApiService(DataDictMapper dataDictMapper, HttpApiMapper httpApiMapper) {
        this.dataDictMapper = dataDictMapper;
        this.httpApiMapper = httpApiMapper;
    }

    public BaseResponse copy(SaveNodeBody body) {
        return ResultUtils.success();
    }

    public List<DataDictModel> getHttpMethodList() {
        return dataDictMapper.selectAll(100002);
    }

    public HttpApi load(Integer id) {
        return httpApiMapper.selectByPrimaryId(id);
    }

    public BaseResponse update(HttpApi httpApi, User user) {
        httpApiMapper.updateBySelective(httpApi);
        return updateNode4CommonFields(user, httpApi, DataTypeEnum.APP_API);
    }
}
