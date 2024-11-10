package com.tm.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
@Component
public class RSAUtils {

    public static String rsaPrivateKey;

    public static String rsaPublicKey;

    @Value("${rsa.privateKey}")
    public void setRsaPrivateKey(String rsaPrivateKey) {
        RSAUtils.rsaPrivateKey = rsaPrivateKey;
    }

    @Value("${rsa.publicKey}")
    public void setRsaPublicKey(String rsaPublicKey) {
        RSAUtils.rsaPublicKey = rsaPublicKey;
    }

    //生成秘钥对
    public static KeyPair getKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    //获取公钥(Base64编码)
    public static String getPublicKey(KeyPair keyPair){
        PublicKey publicKey = keyPair.getPublic();
        byte[] bytes = publicKey.getEncoded();
        return byte2Base64(bytes);
    }

    //获取私钥(Base64编码)
    public static String getPrivateKey(KeyPair keyPair){
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] bytes = privateKey.getEncoded();
        return byte2Base64(bytes);
    }

    //将Base64编码后的公钥转换成PublicKey对象
    public static PublicKey string2PublicKey(String pubStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = base642Byte(pubStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    //将Base64编码后的私钥转换成PrivateKey对象
    public static PrivateKey string2PrivateKey(String priStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = base642Byte(priStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    //公钥加密
    public static byte[] publicEncrypt(byte[] content, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(content);
    }

    //私钥解密
    public static byte[] privateDecrypt(byte[] content, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(content);
    }

    //字节数组转Base64编码
    public static String byte2Base64(byte[] bytes){
        Base64.Encoder encoder = Base64.getMimeEncoder();
        return new String(encoder.encode(bytes));
    }

    //Base64编码转字节数组
    public static byte[] base642Byte(String base64Key) {
        Base64.Decoder decoder = Base64.getMimeDecoder();
        return decoder.decode(base64Key);
    }

    public static String encrypt(String message) {
        return encrypt(rsaPublicKey, message);
    }

    public static String decrypt(String message) {
        return decrypt(rsaPrivateKey, message);
    }

    public static String encrypt(String publicKeyStr, String message) {
        if(StringUtils.isBlank(message)) {
            return message;
        }
        PublicKey publicKey;
        try {
            publicKey = RSAUtils.string2PublicKey(publicKeyStr);
        } catch (Exception e) {
            log.error("encrypt error, ", e);
            return message;
        }
        byte[] publicEncrypt;
        try {
            publicEncrypt = RSAUtils.publicEncrypt(message.getBytes(), publicKey);
        } catch (Exception e) {
            log.error("encrypt error, ", e);
            return message;
        }
        return RSAUtils.byte2Base64(publicEncrypt);
    }

    public static String decrypt(String privateKeyStr, String message) {
        if(StringUtils.isBlank(message)) {
            return message;
        }
        PrivateKey privateKey;
        try {
            privateKey = RSAUtils.string2PrivateKey(privateKeyStr);
        } catch (Exception e) {
            log.error("string2PrivateKey error, ", e);
            return message;
        }
        byte[] base642Byte = RSAUtils.base642Byte(message);
        byte[] privateDecrypt;
        try {
            privateDecrypt = RSAUtils.privateDecrypt(base642Byte, privateKey);
        } catch (Exception e) {
            log.error("privateDecrypt error, ", e);
            return message;
        }
        return new String(privateDecrypt);
    }
}