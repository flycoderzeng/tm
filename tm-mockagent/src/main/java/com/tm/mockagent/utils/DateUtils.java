package com.tm.mockagent.utils;


import com.tm.mockagent.entities.enumerate.DateUnitTypeNum;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class DateUtils {
    public static final String DATE_PATTERN_YMD = "yyyyMMdd";
    public static final String DATE_PATTERN_YM = "yyyyMM";
    public static final String DATE_PATTERN_YMDHMS = "yyyyMMddHHmmss";
    public static final String DATE_PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final int HOUR_IN_MILLISECOND = 3600000;
    public static final int MINUTE_IN_MILLISECOND = 60000;
    public static final int SECOND_IN_MILLISECOND = 1000;
    public static final int DAY_IN_MILLISECOND = HOUR_IN_MILLISECOND * 24;
    public static final Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");

    public static String getDefaultDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN_DEFAULT);
        return dateFormat.format(new Date());
    }

    public static String parseTimestampToFormatDate(Long time, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date date = toDate(time);
        return dateFormat.format(date);
    }

    public static Date addYears(Date date, int years) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.roll(Calendar.YEAR, years);
        return cal.getTime();
    }

    public static Date addMonths(Date date, int months) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int m = cal.get(Calendar.MONTH) + months;
        if (m < 0) {
            m -= 12;
        }
        cal.roll(Calendar.YEAR, m / 12);
        cal.roll(Calendar.MONTH, months);
        return cal.getTime();
    }

    public static Date addDays(Date date, int days) {
        long now = date.getTime() + ((long) days * DAY_IN_MILLISECOND);
        return new Date(now);
    }

    public static Date addHours(Date date, int hours) {
        long now = date.getTime() + ((long) hours * HOUR_IN_MILLISECOND);
        return new Date(now);
    }

    public static Date addMinutes(Date date, int minutes) {
        long now = date.getTime() + ((long) minutes * MINUTE_IN_MILLISECOND);
        return new Date(now);
    }

    public static Date addSeconds(Date date, int seconds) {
        long now = date.getTime() + ((long) seconds * SECOND_IN_MILLISECOND);
        return new Date(now);
    }

    public static Date toDate(String dateString, String format) {
        try {
            Date date = new SimpleDateFormat(format).parse(dateString);
            return date;
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date toDate(String dateString) {
        try {
            return new SimpleDateFormat(getDateFormat(dateString)).parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date toDate(Long time) {
        Date date;
        if (String.valueOf(time).length() == 10) {
            date = new Date(Long.parseLong(time + "000"));
        }else{
            date = new Date(time);
        }
        return date;
    }

    /**
     * 常规自动日期格式识别
     * @param dateString 时间字符串
     * @return Date
     * @author dc
     */
    public static String getDateFormat(String dateString) {
        boolean year = false;
        if(pattern.matcher(dateString.substring(0, 4)).matches()) {
            year = true;
        }
        StringBuilder sb = new StringBuilder();
        int index = 0;
        if(!year) {
            if(dateString.contains("月") || dateString.contains("-") || dateString.contains("/")) {
                if(Character.isDigit(dateString.charAt(0))) {
                    index = 1;
                }
            }else {
                index = 3;
            }
        }
        for (int i = 0; i < dateString.length(); i++) {
            char chr = dateString.charAt(i);
            if(Character.isDigit(chr)) {
                if(index == 0) {
                    sb.append("y");
                }
                if(index == 1) {
                    sb.append("M");
                }
                if(index == 2) {
                    sb.append("d");
                }
                if(index == 3) {
                    sb.append("H");
                }
                if(index == 4) {
                    sb.append("m");
                }
                if(index == 5) {
                    sb.append("s");
                }
                if(index == 6) {
                    sb.append("S");
                }
            }else {
                if(i>0) {
                    char lastChar = dateString.charAt(i-1);
                    if(Character.isDigit(lastChar)) {
                        index++;
                    }
                }
                sb.append(chr);
            }
        }
        String result = sb.toString();
        if(result.equals("yyyyyyyy")) {
            return DATE_PATTERN_YMD;
        }
        if(result.equals("yyyyyyyyyyyyyyyy")) {
            return DATE_PATTERN_YMDHMS;
        }
        return result;
    }

    public static Date addDate(String dateString, int diff, TimeType timeType) {
        String format = getDateFormat(dateString);
        return addDate(dateString, format, diff, timeType);
    }

    public static Date addDate(String dateString, String format, int diff, TimeType timeType) {
        Date date = toDate(dateString, format);
        if(date == null) {
            return null;
        }
        return switch (timeType) {
            case YEAR -> addYears(date, diff);
            case MONTH -> addMonths(date, diff);
            case DAY -> addDays(date, diff);
            case HOUR -> addHours(date, diff);
            case MINUTE -> addMinutes(date, diff);
            case SECOND -> addSeconds(date, diff);
            default -> null;
        };
    }

    public enum TimeType {
        YEAR, MONTH, DAY, HOUR, MINUTE, SECOND
    }

    public static Date addOffset(Date baseDate, DateUnitTypeNum unitTypeNum, Integer offset) {
        if(baseDate == null) {
            baseDate = new Date();
        }
        return switch (unitTypeNum) {
            case SECOND -> DateUtils.addSeconds(baseDate, offset);
            case MINUTE -> DateUtils.addMinutes(baseDate, offset);
            case HOUR -> DateUtils.addHours(baseDate, offset);
            case DAY -> DateUtils.addDays(baseDate, offset);
            case MONTH -> DateUtils.addMonths(baseDate, offset);
            case YEAR -> DateUtils.addYears(baseDate, offset);
        };
    }

    public static Date stringDateToDate(String timeString) {
        Date baseDate = new Date();
        if(StringUtils.isBlank(timeString)) {
            return baseDate;
        }
        if(StringUtils.isNumeric(timeString) && timeString.length() == 13) {
            baseDate = new Date(Long.parseLong(timeString));
        }else if(StringUtils.isNumeric(timeString) && timeString.length() == 10) {
            baseDate = new Date(Long.parseLong(timeString) * 1000);
        }else if(StringUtils.isNotBlank(timeString)) {
            baseDate = DateUtils.toDate(timeString, DateUtils.getDateFormat(timeString));
        }
        return baseDate;
    }
}
