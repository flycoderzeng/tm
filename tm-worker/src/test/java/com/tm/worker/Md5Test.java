package com.tm.worker;

import cn.hutool.crypto.SecureUtil;

public class Md5Test {
    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        final String s = SecureUtil.md5("123456");
        System.out.println(System.currentTimeMillis());
        System.out.println(s);
    }
}
