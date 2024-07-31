package com.lpc.cryptography.base;

/**
 * @author lpc
 * @version 1.0
 * @since 2024-04-14
 */

import com.lpc.cryptography.common.PairingProvider;
import com.lpc.util.CustomSerializer;
import it.unisa.dia.gas.jpbc.Element;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class AES {
    // 使用AES密钥加密数据,返回Base64加密字符串
    public static String encrypt(List<Element> signature, SecretKey secretKey) throws Exception {
        byte[] signa = CustomSerializer.serialize(signature);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // 使用默认提供者
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(signa);
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // 使用AES密钥解密数据
    public static List<Element> decrypt(String encryptedText, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // 使用默认提供者
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] bytes = cipher.doFinal(encryptedBytes);
        List<Element> decryptedList = (List<Element>) CustomSerializer.deserialize(bytes);
        return decryptedList;
    }

    public static void main(String[] args) throws Exception {
//        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
//        keyGen.init(128); // for example
//        SecretKey key = keyGen.generateKey();
//
//        List<Object> list = new ArrayList<>();
//        list.add(5);
//        list.add("123");
//        list.add(PairingProvider.G.newRandomElement().getImmutable());
//        String encrypt = encrypt(list, key);
//
//        List<Object> decrypt = decrypt(encrypt, key);
//        System.out.println(decrypt);
    }
}
