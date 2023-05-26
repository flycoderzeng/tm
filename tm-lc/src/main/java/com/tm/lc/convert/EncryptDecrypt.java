package com.tm.lc.convert;

import com.tm.common.utils.RSAUtils;
import jakarta.persistence.AttributeConverter;


public class EncryptDecrypt implements AttributeConverter<String, String> {
    @Override
    public String convertToDatabaseColumn(String s) {
        return RSAUtils.encrypt(s);
    }

    @Override
    public String convertToEntityAttribute(String s) {
        return RSAUtils.decrypt(s);
    }
}
