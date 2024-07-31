package com.lpc.cryptography.util;

import com.lpc.cryptography.common.PairingProvider;

import java.io.*;

public class Serialize implements PairingProvider {
    // 序列化List
    public static byte[] serialize(Object list) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(list);
        oos.close();
        return bos.toByteArray();
    }

    // 反序列化
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object o = ois.readObject();
        ois.close();
        return o;
    }

}
