package com.tm.web;

import com.tm.web.utils.RSAUtils;

import java.security.PrivateKey;
import java.security.PublicKey;

public class RSATest2 {
    public static String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsWf37bYAFpQHRtVZN72k+mCZZZGA/NXF\n" +
            "n5RLA4Z3CRUkkiJPfmgTehcJ+fs8NXrRpOO2CYKTOv2NYngV6+EeIcaiG+qHtZtkmYf5OoiZq+o1\n" +
            "ASgAK+0JV7xnJhY+dQBWwp8ozpoKoyCTBqymXUv30YodbJ88JwNim2RCJY4b+2gOrGZ1PCXu6PlH\n" +
            "CHyciiOXLxQIWZ0iBr7XhD9d3ECBv5x51I1ArGjynoHCS5RYKYZXLngDlO0Ss+/vnOiy7sL5B700\n" +
            "Bu7bHXCPyGVTh7qyqJD7byAttP8aOCghpq/aM/9+I10N3bp+4t4rBnidfRvRJQ7TudEfYTV5gRoq\n" +
            "pefUrwIDAQAB";
    public static String privateKeyStr = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCxZ/fttgAWlAdG1Vk3vaT6YJll\n" +
            "kYD81cWflEsDhncJFSSSIk9+aBN6Fwn5+zw1etGk47YJgpM6/Y1ieBXr4R4hxqIb6oe1m2SZh/k6\n" +
            "iJmr6jUBKAAr7QlXvGcmFj51AFbCnyjOmgqjIJMGrKZdS/fRih1snzwnA2KbZEIljhv7aA6sZnU8\n" +
            "Je7o+UcIfJyKI5cvFAhZnSIGvteEP13cQIG/nHnUjUCsaPKegcJLlFgphlcueAOU7RKz7++c6LLu\n" +
            "wvkHvTQG7tsdcI/IZVOHurKokPtvIC20/xo4KCGmr9oz/34jXQ3dun7i3isGeJ19G9ElDtO50R9h\n" +
            "NXmBGiql59SvAgMBAAECggEBAITZe6MqOJSv02cq2z3PqVzJu/WAaZxspSokTaxdJITLrXA1e1wI\n" +
            "qhUybYkip2rRB284CpjCjGiPeTH1BU13bi4S5lCLZ4cKz+HdHjjEFHfV0k2EW+SJBAcMkh3GaI0L\n" +
            "bO0fbNqSJkcUXKWR4p9ZBUKH8BLoh44bjdsZ0CL5cWDowvQQ8CDrSzgfsdX68Ko5gdOiNr+Rq7p9\n" +
            "VOpq/8WVVap/6fNrPl7jbGwrjsJOfaC224JLG51qxeylmOvoPxT2SmpSZ7KghFF1QtB3it+L8hzh\n" +
            "aOa9hp1qngQTBHdGttRQ6djkSYIOI43kq+CnitY7cUYBHtF3o39Kwk+SPBLU2YECgYEA4R1LQDmu\n" +
            "SgO6qx4yQ1wSHeFOhQaV4DlMvpHYK/nP+x8xBEDs2GsFaPdXMKvIiMOAn6U8A9XEDqU8KBkF1sAq\n" +
            "/4HoGp9iDa9sgr7B3kbC7dXFWpshKuQHyuYdvfEPzQbEqqSYh0YtlgIhPZh49c8N2Yzj34mJaqpm\n" +
            "xXdP1uKSjEECgYEAyb8DZh3XG1Wens6FIfLw0HpNhUa/O/BGSfXiAETy3TUgxngdZrHvxq+MiVIR\n" +
            "HTl7q/svsGTL3eHllODEQD1mmGW+IxNI7v3TdFuxs7KTDe+WLZLGTDBe7zjp3PNCjdjI2WwCWjXB\n" +
            "2L5kizXdKuLw65LgXJ70/x6hlZwhbkLt5O8CgYB0xEcmN0UqayjGXdFgkz9qdFXQNoxDVDV0nB3v\n" +
            "IZM6BQHd3Pd9gnW57qIxQsmv9wiexoyMyLL7jcXYXrJipCcphW6pcIbh5l54qw3QiA/QYhQOXW0k\n" +
            "3EcWfaprvcCebqZQhTQA5uv6ZXxsgMwtZIsgjy4b4zF+PIk2YB7vrXKqgQKBgCoaR+5Oue6FbsX7\n" +
            "mYMdPS5056qxugUbPr/+pXyi5wBNLxrcRF6DMeQIDM83pkSwE2Qv1qL8ViNnGk94nNw/NCMH0Kdn\n" +
            "RehCZpe47xvuX4wf/Ppj0ChkS9umx/+JcNFDdZ8SUGddIqhGC5uV0SM7G0Y+b75EmiKH1PbxD3UA\n" +
            "WL9XAoGAOEJMv8xLcNqSePRrQiYQdxSQG7RZS0Pp6nBVXhNMKqWsRwqC3qsv3yntixMYFsVdeq3r\n" +
            "UiT48nFHMx7h5vdvHCExo4ZLarwFpcgcH9PpAAl3Zkna4sMA9VP+XioSvqbPA6ZoSYQlDbODKJ3m\n" +
            "vupCYYy74GVetYE8W0m68LBh/IA=";
    public static void main(String[] args) throws Exception {
        System.out.println(privateKeyStr);
        String message = "hello, i am infi, good night!";
        // 加密
        //将Base64编码后的公钥转换成PublicKey对象
        PublicKey publicKey = RSAUtils.string2PublicKey(publicKeyStr);
        //用公钥加密
        byte[] publicEncrypt = RSAUtils.publicEncrypt(message.getBytes(), publicKey);
        //加密后的内容Base64编码
        String byte2Base64 = RSAUtils.byte2Base64(publicEncrypt);
        System.out.println("公钥加密并Base64编码的结果：" + byte2Base64);

        //将Base64编码后的私钥转换成PrivateKey对象
        PrivateKey privateKey = RSAUtils.string2PrivateKey(privateKeyStr);
        //加密后的内容Base64解码
        byte[] base642Byte = RSAUtils.base642Byte(byte2Base64);
        //用私钥解密
        byte[] privateDecrypt = RSAUtils.privateDecrypt(base642Byte, privateKey);
        //解密后的明文
        System.out.println("解密后的明文: " + new String(privateDecrypt));
    }
}
