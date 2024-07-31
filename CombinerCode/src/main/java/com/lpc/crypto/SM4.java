package com.lpc.crypto;

import com.lpc.util.Serialize;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.security.Security;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class SM4 {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    // 使用SM4密钥加密数据,返回Base64加密字符串
    public static String encrypt(List<Object>signature, SecretKey secretKey) throws Exception {
        byte[]signa= Serialize.serialize(signature);
        Cipher cipher = Cipher.getInstance("SM4", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(signa);
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // 使用SM4密钥解密数据
    public static List<Object> decrypt(String encryptedText, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("SM4", "BC");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] bytes = cipher.doFinal(encryptedBytes);
        List<Object> decryptedList = (List<Object>) Serialize.deserialize(bytes);
        return decryptedList;
    }

    public static void main(String[] args) throws Exception {
        byte[]Key=new byte[16];
        byte[] bytes = Hash.calculateSM3Hash("123".getBytes());
        System.arraycopy(bytes,0,Key,0,16);
        SecretKey key = new SecretKeySpec(Key, "SM4");
        List<Object>list=new ArrayList<>();
        list.add(5);
        list.add("123");
        String encrypt = encrypt(list,key);

        List<Object> decrypt = decrypt(encrypt, key);
        System.out.println(decrypt);
    }

}

