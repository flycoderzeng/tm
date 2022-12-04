package com.tm.common.base.mapper;

import com.tm.common.base.model.Right;

public interface RightMapper {
    Right findByURI(String uri);
}
