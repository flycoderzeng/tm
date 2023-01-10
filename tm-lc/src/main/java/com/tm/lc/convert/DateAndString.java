package com.tm.lc.convert;

import com.tm.common.utils.DateUtils;
import jakarta.persistence.AttributeConverter;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

public class DateAndString implements AttributeConverter<String, Date> {
    @Override
    public Date convertToDatabaseColumn(String s) {
        if(s == null) {
            return null;
        }
        return DateUtils.toDate(s, DateUtils.DATE_PATTERN_DEFAULT);
    }

    @Override
    public String convertToEntityAttribute(Date date) {
        if(date == null) {
            return null;
        }
        return DateFormatUtils.format(date, DateUtils.DATE_PATTERN_DEFAULT);
    }
}
