package com.lpc.crypto;


import java.security.NoSuchAlgorithmException;

public class Test {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String s = Hash.calculateSM3Hash("123456");
        System.out.println(s);
    }
}
