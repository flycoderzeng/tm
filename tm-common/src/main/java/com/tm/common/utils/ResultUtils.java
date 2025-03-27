package com.tm.common.utils;

import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.common.enumerate.ResultCodeEnum;

public class ResultUtils {
    private ResultUtils() {}
    public static BaseResponse success(Object object) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(ResultCodeEnum.SUCCESS.getCode());
        baseResponse.setMessage(ResultCodeEnum.SUCCESS.getMsg());
        baseResponse.setData(object);
        return baseResponse;
    }

    public static BaseResponse success() {
        return success(null);
    }

    public static BaseResponse error(ResultCodeEnum resultEnum) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(resultEnum.getCode());
        baseResponse.setMessage(resultEnum.getMsg());
        return baseResponse;
    }

    public static BaseResponse error(int code, String msg) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(code);
        baseResponse.setMessage(msg);
        return baseResponse;
    }

    public static BaseResponse error(String msg) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(ResultCodeEnum.ERROR.getCode());
        baseResponse.setMessage(msg);
        return baseResponse;
    }
}
