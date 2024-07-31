package com.lpc.util.encoders;

import com.lpc.cryptography.common.PairingProvider;
import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.asn1.*;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.io.BufferingOutputStream;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;

/**
 * @author lpc
 * @version 1.0
 * @since 2024-05-13
 */
public class HexDERCodec implements PairingProvider {

    public static void encodeToDER(String hexString,String outputPath){
        BigInteger bigInteger = new BigInteger(hexString, 16);
        ASN1Integer asn1Integer = new ASN1Integer(bigInteger);
        try (BufferingOutputStream bos = new BufferingOutputStream(new FileOutputStream(outputPath))) {
            // 写入 ASN.1 编码数据
            ASN1OutputStream asn1OutputStream = ASN1OutputStream.create(bos, ASN1Encoding.DER);
            asn1OutputStream.writeObject(asn1Integer);
            asn1OutputStream.close();
            System.out.println("Encoded to DER and saved to " + outputPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decodeFromDER(MultipartFile file) {
        if (file.isEmpty()) {
            return "File is empty";
        }

        try (ASN1InputStream asn1InputStream = new ASN1InputStream(file.getInputStream())) {
            ASN1Primitive object = asn1InputStream.readObject();

            if (object instanceof ASN1Integer) {
                ASN1Integer asn1Integer = (ASN1Integer) object;

                // 获取 BigInteger 值并转换为十六进制字符串
                String hexString = asn1Integer.getValue().toString(16);
                // 确保输出十六进制字符串长度为偶数，以正确表示每个字节
                if (hexString.length() % 2 != 0) {
                    hexString = "0" + hexString; // 添加前导零以保持字节完整性
                }

                return hexString;
            } else {
                throw new IllegalArgumentException("The uploaded file does not contain an ASN1Integer.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading file: " + e.getMessage();
        }
    }

    public static String decodeFromDER(String derFilePath) {
        try (FileInputStream fis = new FileInputStream(derFilePath)) {
            ASN1InputStream asn1InputStream = new ASN1InputStream(fis);
            ASN1Primitive object = asn1InputStream.readObject();
            if (object instanceof ASN1Integer) {
                ASN1Integer asn1Integer = (ASN1Integer) object;

                // 获取 BigInteger 值并转换为十六进制字符串
                BigInteger bigInt = asn1Integer.getValue();
                String hexString = bigInt.toString(16);

                // 确保输出十六进制字符串长度为偶数，以正确表示每个字节
                if (hexString.length() % 2 != 0) {
                    hexString = "0" + hexString; // 添加前导零以保持字节完整性
                }

                return hexString;
            } else {
                throw new IllegalArgumentException("The DER file does not contain an ASN1Integer.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) {

        String hexString="046677105232b40ae71b42168de21b58d14ca93a6788aed246a4ad3b34e4f43fb057626522c39f3374858d407c9bd81fbe3b4d3244ccb6bbcca5bcf51083b91881fee6259793e9be5b30e7a4e5273ff4a4fa19888f8b2c22c25ed4d420c2635bf51ed65de0f1a4640417b34caaf1bbbe3c9455399d0ec0a04d72aeec896992c2";
        // 测试编码
        String derPath = "kagg.der";
        encodeToDER(hexString, derPath);

        // 测试解码
        String decodedHexString = decodeFromDER(derPath);
        System.out.println(decodedHexString);
        System.out.println("Decoded Hex String: " + decodedHexString.length());


    }
}
