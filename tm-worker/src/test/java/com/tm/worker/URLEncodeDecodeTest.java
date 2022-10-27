package com.tm.worker;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class URLEncodeDecodeTest {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String s = "a=1&b=2!";
        String js = "a%3D1%26b%3D2!";
        final String decode = URLDecoder.decode(js, "UTF-8");
        System.out.println(decode);;

        final String encode = URLEncoder.encode(s, "UTF-8");
        System.out.println(encode);
    }
}
