package com.tm.web;

import com.tm.common.entities.common.enumerate.DataTypeEnum;

public class EnumTest1 {
    public static void main(String[] args) {
        System.out.println(DataTypeEnum.AUTO_CASE.value());
        int[][] c = new int[2][3];
        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < c[i].length; j++) {
                System.out.println(c[i][j]);
            }
        }
        String s = "111";
        byte[] bytes = s.getBytes();
        s.charAt(0);
    }
}
