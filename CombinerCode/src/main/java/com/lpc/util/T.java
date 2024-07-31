package com.lpc.util;

import org.bouncycastle.util.encoders.Base64;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class T {

    public static void main(String[] args) {
        try {
            // 生成密钥对
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            ECGenParameterSpec secp256r1 = new ECGenParameterSpec("secp256r1");
            keyPairGenerator.initialize(secp256r1,new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
            byte[]data=new byte[900*1024];
            Signature ecdsa = Signature.getInstance("SHA256withECDSA");
            ecdsa.initSign(privateKey);
            ecdsa.update(data);
            byte[] sign = ecdsa.sign(); //返回签名值
            String encodedSign = Base64.toBase64String(sign);

            Signature verifySign = Signature.getInstance("SHA256withECDSA");
            verifySign.initVerify(publicKey);
            verifySign.update(data);
            boolean result= verifySign.verify(Base64.decode(encodedSign));
            System.out.println("result = " + result);
//            // 待签名数据
//            byte[]data=new byte[900*1024];
//            // 生成签名
//            Signature ecdsaSign = Signature.getInstance("SHA256withECDSA");
//            long t1 = System.currentTimeMillis();
//            ecdsaSign.initSign(privateKey);
//            ecdsaSign.update(data);
//            byte[] signature = ecdsaSign.sign();
//            long t2 = System.currentTimeMillis();
//            System.out.println("t2-t1 = " + (t2 - t1));
//            String encodedSignature = Base64.getEncoder().encodeToString(signature);
//            System.out.println("ECDSA Signature: " + encodedSignature);
//
//            // 验证签名
//            Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA");
//            ecdsaVerify.initVerify(publicKey);
//            ecdsaVerify.update(data);
//            boolean result = ecdsaVerify.verify(Base64.getDecoder().decode(encodedSignature));
//            System.out.println("Signature valid: " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
