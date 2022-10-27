package com.tm.worker;

import cn.binarywang.tools.generator.ChineseAddressGenerator;
import cn.binarywang.tools.generator.ChineseIDCardNumberGenerator;

public class GeneratorTest {
    public static void main(String[] args) {
        String generatedAddress = ChineseAddressGenerator.getInstance()
                .generate();
        System.err.println(generatedAddress);
        String idcard = ChineseIDCardNumberGenerator.getInstance().generate();
        System.out.println(idcard);
        String birthday = "19870313";
        idcard = idcard.substring(0, 6) + birthday + idcard.substring(14);
        System.out.println(idcard);
        String issueOrg = ChineseIDCardNumberGenerator.generateIssueOrg();
        System.err.println(issueOrg);
        String result = ChineseIDCardNumberGenerator.generateValidPeriod();
        System.err.println(result);
    }
}
