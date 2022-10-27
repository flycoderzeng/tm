package com.tm.common.base.mapper;

import com.tm.common.base.model.DataDictModel;

import java.util.List;

public interface DataDictMapper {
    List<DataDictModel> selectAll(Integer dataTypeId);
}
