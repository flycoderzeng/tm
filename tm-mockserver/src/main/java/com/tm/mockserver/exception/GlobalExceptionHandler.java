package com.tm.mockserver.exception;


import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.common.enumerate.ResultCodeEnum;
import com.tm.common.utils.ResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Locale;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(value = Exception.class)
    public BaseResponse handle(Exception e) throws Exception {
        if (e instanceof MethodArgumentNotValidException) {
            StringBuilder detailInfo = new StringBuilder(512);
            detailInfo.append("参数错误:[");
            for (FieldError fieldError : ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors()) {
                detailInfo.append(messageSource.getMessage(fieldError,Locale.CHINA));
                detailInfo.append(";");
            }
            String message = detailInfo.substring(0, detailInfo.length()-1).concat("]");
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR.getCode(), message);
        } else if (!(e instanceof HttpServerErrorException.InternalServerError)) {
            logger.error("[系统异常] {}", e);
            return ResultUtils.error(ResultCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
        }
        throw e;
    }
}


