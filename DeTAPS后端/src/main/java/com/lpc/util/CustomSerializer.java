package com.lpc.util;

import com.lpc.cryptography.common.PairingProvider;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CustomSerializer {

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        serializeObject(oos, obj);
        oos.close();
        return bos.toByteArray();
    }

    private static void serializeObject(ObjectOutputStream oos, Object obj) throws IOException {
        if (obj instanceof Element) {
            oos.writeInt(-1); // Element标识符
            Element element = (Element) obj;
            int filedIndex=-1;
            Field field = element.getField();
            if(field.equals(PairingProvider.G)){
                filedIndex=1;
            } else if (field.equals(PairingProvider.G2)) {
                filedIndex=2;
            } else if (field.equals(PairingProvider.GT)){
                filedIndex=3;
            } else {
                if(element.equals(PairingProvider.Zr.newElement(element))){
                    filedIndex=0;
                }
            }
            oos.writeInt(filedIndex);
            byte[] elementBytes = element.toBytes();
            oos.writeInt(elementBytes.length);
            oos.write(elementBytes);
        } else if (obj instanceof List) {
            oos.writeInt(-2); // List开始标识符
            List<?> list = (List<?>) obj;
            for (Object item : list) {
                serializeObject(oos, item);
            }
            oos.writeInt(-3); // List结束标识符
        } else if (obj instanceof String) {
            oos.writeInt(-4);
            oos.writeUTF((String) obj);
        } else if (obj instanceof Integer) {
            oos.writeInt(-5);
            oos.writeInt((Integer) obj);
        }
    }

    public static Object deserialize(byte[] data) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object obj = deserializeObject(ois);
        ois.close();
        return obj;
    }

    private static Object deserializeObject(ObjectInputStream ois) throws IOException {
        int type = ois.readInt();
        switch (type) {
            case -1:
                return deserializeElement(ois);
            case -2:
                return deserializeList(ois);
            case -4:
                return ois.readUTF();
            case -5:
                return ois.readInt();
            default:
                throw new IOException("Unknown type: " + type);
        }
    }

    private static Element deserializeElement(ObjectInputStream ois) throws IOException {
        int fieldIndex = ois.readInt();
        int length = ois.readInt();
        byte[] elementBytes = new byte[length];
        ois.readFully(elementBytes);

        return PairingProvider.pairing.getFieldAt(fieldIndex).newElementFromBytes(elementBytes).getImmutable();
    }

    private static List<Object> deserializeList(ObjectInputStream ois) throws IOException {
        List<Object> list = new ArrayList<>();
        while (true) {
            int type = ois.readInt(); // 直接读取下一个对象的类型
            if (type == -3) { // 检查是否到达列表的结束
                break;
            } else {
                // 如果不是结束标识符，需要处理当前读取的类型
                // 由于已经读取了类型信息，需要将这个类型信息传递给反序列化对象的方法
                list.add(deserializeObjectByType(ois, type));
            }
        }
        return list;
    }

    // 根据类型标识符反序列化对象的辅助方法
    private static Object deserializeObjectByType(ObjectInputStream ois, int type) throws IOException {
        switch (type) {
            case -1:
                return deserializeElement(ois);
            case -4:
                return ois.readUTF();
            case -5:
                return ois.readInt();
            case -2:
                // 对于嵌套列表的情况，再次调用反序列化列表的方法
                return deserializeList(ois);
            default:
                throw new IOException("Unknown type: " + type);
        }
    }
}





