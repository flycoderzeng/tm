package com.tm.common.dao;

import com.tm.common.base.mapper.DataNodeMapper;
import com.tm.common.base.model.DataNode;
import jakarta.inject.Inject;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

@Component
public class DataNodeDao {
    private final DataNodeMapper dataNodeMapper;

    @Inject
    public DataNodeDao(DataNodeMapper dataNodeMapper) {
        Assert.notNull(dataNodeMapper, "DataNodeMapper must not be null!");
        this.dataNodeMapper = dataNodeMapper;
    }

    public List<DataNode> selectByDataTypeIdAndName(Integer dataTypeId, String name) {
        return dataNodeMapper.selectByDataTypeIdAndName(dataTypeId, name);
    }

}
