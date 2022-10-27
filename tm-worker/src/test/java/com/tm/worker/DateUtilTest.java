package com.tm.worker;

import com.tm.common.utils.DateUtils;

public class DateUtilTest {
    public static void main(String[] args) {
        String s = "20211118";
        String dateFormat = DateUtils.getDateFormat(s);
        System.out.println(dateFormat);
    }
}
