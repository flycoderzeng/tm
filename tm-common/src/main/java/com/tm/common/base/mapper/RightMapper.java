package com.tm.common.base.mapper;

import com.tm.common.base.model.Right;
import com.tm.common.entities.base.CommonTableQueryBody;

import java.util.List;

public interface RightMapper {
    Right findByURI(String uri);
}
