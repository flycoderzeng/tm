package com.tm.web.exception;

import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.common.enumerate.ResultCodeEnum;
import com.tm.common.utils.ResultUtils;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    @Inject
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(value = Exception.class)
    public BaseResponse handle(Exception e){
        if (e instanceof MethodArgumentNotValidException) {
            StringBuilder stringBuilder = new StringBuilder(512);
            stringBuilder.append("参数错误:[");
            for (FieldError fieldError : ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors()) {
                stringBuilder.append(messageSource.getMessage(fieldError, Locale.CHINA));
                stringBuilder.append(";");
            }
            String message = stringBuilder.substring(0, stringBuilder.length()-1).concat("]");
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR.getCode(), message);
        } else {
            log.error("[系统异常] {}", e);
            return ResultUtils.error(ResultCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
        }
    }
}
