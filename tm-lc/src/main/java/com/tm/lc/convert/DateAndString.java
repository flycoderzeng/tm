package com.tm.lc.convert;

import com.tm.common.utils.DateUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.persistence.AttributeConverter;
import java.util.Date;

public class DateAndString implements AttributeConverter<String, Date> {
    @Override
    public Date convertToDatabaseColumn(String s) {
        return null;
    }

    @Override
    public String convertToEntityAttribute(Date date) {
        if(date == null) {
            return null;
        }
        return DateFormatUtils.format(date, DateUtils.DATE_PATTERN_DEFAULT);
    }
}
