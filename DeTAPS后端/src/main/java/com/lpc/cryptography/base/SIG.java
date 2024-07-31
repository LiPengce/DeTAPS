package com.lpc.cryptography.base;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class SIG {
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(256); // 或适合您需求的其他安全级别
        return keyPairGenerator.generateKeyPair();
    }

    public static byte[] generateSignature(byte[] message, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initSign(privateKey);
        signature.update(message);
        return signature.sign();
    }

    public static boolean verifySignature(byte[] message, byte[] signatureBytes, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initVerify(publicKey);
        signature.update(message);
        return signature.verify(signatureBytes);
    }

    public static ECPrivateKey recoverPrivateKey(byte[] keyBytes) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        return (ECPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    public static ECPublicKey recoverPublicKey(byte[] keyBytes) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        return (ECPublicKey) keyFactory.generatePublic(keySpec);
    }

    public static void main(String[] args) throws Exception {
        KeyPair keyPair = generateKeyPair();
        ECPrivateKey aPrivate = (ECPrivateKey) keyPair.getPrivate();
        ECPublicKey aPublic = (ECPublicKey) keyPair.getPublic();
        byte[] bytes = generateSignature("123".getBytes(StandardCharsets.UTF_8), recoverPrivateKey(aPrivate.getEncoded()));
        System.out.println(verifySignature("123".getBytes(StandardCharsets.UTF_8), bytes, recoverPublicKey(aPublic.getEncoded())));
    }
}
