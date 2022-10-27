package com.tm.worker;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class BracketTest {
    public static void main(String[] args) {
        int n = 3;
        System.out.println(generateParenthesis(n));
    }

    public static List<String> generateParenthesis(int n) {
        List<byte[]> all = getAll(n);
        all = getValid(all);
        return transform(all);
    }

    public static List<String> transform(List<byte[]> all) {
        final List<String> list = new ArrayList<>();
        for (byte[] bytes : all) {
            list.add(getBracket(bytes));
        }
        Collections.sort(list);
        return list;
    }

    public static String getBracket(byte[] bytes) {
        String s = "";
        for (byte aByte : bytes) {
            if(aByte == 0) {
                s += "(";
            }else{
                s += ")";
            }
        }
        return s;
    }

    public static List<byte[]> getValid(List<byte[]> all) {
        final List<byte[]> list = new ArrayList<>();
        for (byte[] bytes : all) {
            if(isValid(bytes)) {
                list.add(bytes);
            }
        }
        return list;
    }

    public static boolean isValid(byte[] bytes) {
        int j = 0;
        final Stack<Byte> stack = new Stack<>();

        while (j < bytes.length) {
            if(bytes[j] == 0) {
                stack.push(bytes[j]);
            }
            if(bytes[j] == 1) {
                if(stack.isEmpty()) {
                    return false;
                }
                final Byte pop = stack.pop();
                if(pop != 0) {
                    return false;
                }
            }
            j++;
        }
        return stack.isEmpty();
    }

    public static List<byte[]> getAll(int n) {
        final List<byte[]> list = new ArrayList<>();

        int r = (1 << n*2) - 1;
        final int size = 2 * n;
        for (int i = 0; i <= r; i++) {
            list.add(getBytes(i, size));
        }

        return list;
    }

    public static byte[] getBytes(int n, int size) {
        final byte [] bytes = new byte[size];
        int j = 0;
        while(j < size) {
            bytes[j] = (byte)(n & 1);
            j++;
            n = n >> 1;
        }
        return bytes;
    }
}
