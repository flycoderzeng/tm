package com.tm.worker;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

public class SubStringTest {
    public static void main(String[] args) {
        final String abcdef = "abcdef";
        final String sub = StrUtil.sub(abcdef, 0, -1);
        System.out.println(sub);
        String init = "Bob is a Bird... Bob is a Plane... Bob is Superman!";
        final String replace = init.replace("Bob", "SB");
        System.out.println(replace);

        int i = init.indexOf("1000");
        System.out.println(i);
        i = init.indexOf("Plane");
        System.out.println(i);
        System.out.println("Bob is a Bird... Bob is a ".length());
        System.out.println(StrUtil.sub(init, 31, 34));
    }
}
