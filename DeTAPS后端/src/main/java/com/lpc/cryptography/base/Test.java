package com.lpc.cryptography.base;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Test {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        KeyPairGenerator rsa = KeyPairGenerator.getInstance("RSA");
        rsa.initialize(2048);
        KeyPair pair=rsa.generateKeyPair();
        System.out.println(Base64.getEncoder().encodeToString(pair.getPublic().getEncoded()));
        System.out.println();
        System.out.println(Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded()));
    }
}
