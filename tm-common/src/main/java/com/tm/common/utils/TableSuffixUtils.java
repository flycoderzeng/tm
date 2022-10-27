package com.tm.common.utils;

import java.util.Date;

public class TableSuffixUtils {
    public static String getTableSuffix(Date currDate, Integer splitTableType, Integer offset) {
        String suffix;
        Date date;
        if (splitTableType != null && splitTableType.equals(1)) {
            date = DateUtils.addMonths(currDate, offset);
            suffix = DateUtils.parseTimestampToFormatDate(date.getTime(), DateUtils.DATE_PATTERN_YM);
        } else {
            date = DateUtils.addDays(currDate, offset);
            suffix = DateUtils.parseTimestampToFormatDate(date.getTime(), DateUtils.DATE_PATTERN_YMD);
        }
        return suffix;
    }
}
