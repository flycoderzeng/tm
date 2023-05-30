package com.tm.common.dao;

import com.tm.common.base.mapper.DataNodeMapper;
import com.tm.common.base.model.DataNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataNodeDao {
    @Autowired
    private DataNodeMapper dataNodeMapper;

    public List<DataNode> selectByDataTypeIdAndName(Integer dataTypeId, String name) {
        return dataNodeMapper.selectByDataTypeIdAndName(dataTypeId, name);
    }

}
