package com.tm.worker.core.function.date;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import com.tm.common.entities.autotest.enumerate.DateUnitTypeNum;
import com.tm.common.utils.DateUtils;
import com.tm.worker.core.exception.TMException;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static com.tm.common.entities.autotest.enumerate.DateUnitTypeNum.DAY;

public class GetDate extends AbstractFunction {

    @Override
    public AviatorObject call(Map<String, Object> env) {
        return new AviatorString(DateUtils.getDefaultDate());
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        String format = FunctionUtils.getStringValue(arg1, env);
        SimpleDateFormat f = new SimpleDateFormat(format);
        return new AviatorString(f.format(new Date()));
    }

    private Integer getOffset(Map<String, Object> env,  AviatorObject arg) {
        Integer offset = 0;
        if(arg == null) {
            return offset;
        }
        if(arg instanceof AviatorString) {
            String offsetString = arg.toString();
            if(!StringUtils.isNotBlank(offsetString) && !offsetString.startsWith("-") && !StringUtils.isNumeric(offsetString)) {
                throw new TMException("[" + arg + "]参数值类型错误，必须是数字。当前的值是：" + offsetString);
            }
            if(!StringUtils.isNotBlank(offsetString) && offsetString.startsWith("-") && !StringUtils.isNumeric(offsetString.substring(1))) {
                throw new TMException("[" + arg + "]参数值类型错误，必须是数字。当前的值是：" + offsetString);
            }
            offset = Integer.valueOf(offsetString);
        } else {
            offset = FunctionUtils.getNumberValue(arg, env).intValue();
        }
        return offset;
    }

    private Integer getUnit(Map<String, Object> env,  AviatorObject arg) {
        Integer unit = DAY.val();
        if(arg == null) {
            return unit;
        }
        if(arg instanceof AviatorString) {
            String unitString = FunctionUtils.getStringValue(arg, env);
            if(!StringUtils.isNotBlank(unitString) && !StringUtils.isNumeric(unitString)) {
                throw new TMException("[" + arg + "]参数值类型错误，必须是数字。当前的值是：" + unitString);
            }
            unit = Integer.valueOf(unitString);
        } else {
            unit = FunctionUtils.getNumberValue(arg, env).intValue();
        }
        DateUnitTypeNum unitTypeNum = DateUnitTypeNum.get(unit);
        if(unitTypeNum == null) {
            throw new TMException("[unit]参数值错误。必须是：1-秒；2-分；3-时；4-日；5-月；6-年。");
        }
        return unit;
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        String format = FunctionUtils.getStringValue(arg1, env);
        Integer offset = getOffset(env, arg2);

        Date date = DateUtils.addDays(new Date(), offset);
        SimpleDateFormat f = new SimpleDateFormat(format);
        return new AviatorString(f.format(date));
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
        String format = FunctionUtils.getStringValue(arg1, env);
        Integer offset = getOffset(env, arg2);
        Integer unit = getUnit(env, arg3);

        Date date = DateUtils.addOffset(new Date(), DateUnitTypeNum.get(unit), offset);
        SimpleDateFormat f = new SimpleDateFormat(format);
        return new AviatorString(f.format(date));
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3, AviatorObject arg4) {
        String format = FunctionUtils.getStringValue(arg1, env);
        Integer offset = getOffset(env, arg2);
        Integer unit = getUnit(env, arg3);
        Date baseDate = DateUtils.stringDateToDate(arg4.stringValue(env));
        Date date = DateUtils.addOffset(baseDate, DateUnitTypeNum.get(unit), offset);

        SimpleDateFormat f = new SimpleDateFormat(format);
        return new AviatorString(f.format(date));
    }

    /**
     * 返回方法名
     */
    @Override
    public String getName() {
        return "__getDate";
    }
}
