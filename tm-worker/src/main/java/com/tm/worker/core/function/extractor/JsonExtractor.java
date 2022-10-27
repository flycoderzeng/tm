package com.tm.worker.core.function.extractor;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import com.jayway.jsonpath.JsonPath;
import com.tm.worker.core.exception.CommonValueBlankException;
import com.tm.worker.core.variable.AutoTestVariables;
import com.tm.worker.utils.AviatorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Slf4j
public class JsonExtractor extends AbstractFunction {

    private String getPath(Map<String, Object> env, AviatorObject arg1) {
        String path = AviatorUtils.getArgStringValue(env, arg1);
        if(StringUtils.isBlank(path)) {
            throw new CommonValueBlankException("json路径不能为空");
        }

        return path;
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        String path = getPath(env, arg1);
        log.debug("json path: {}", path);
        String resultString = "";
        Object o = env.get(AutoTestVariables.BUILTIN_VARIABLE_NAME_RESPONSE);
        if(o != null) {
            Object result = JsonPath.read(o, path);
            resultString = result.toString();
        }
        log.debug("json提取结果: {}", resultString);

        return new AviatorString(resultString);
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        String path = getPath(env, arg1);
        log.debug("json path: {}", path);
        String resultString = "";
        Object javaObject = FunctionUtils.getJavaObject(arg2, env);
        if(javaObject instanceof String) {
            Object result = JsonPath.read(javaObject.toString(), path);
            resultString = result.toString();
        }
        return new AviatorString(resultString);
    }

    @Override
    public String getName() {
        return "__jsonExtractor";
    }
}
