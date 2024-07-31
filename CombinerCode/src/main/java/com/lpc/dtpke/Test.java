package com.lpc.dtpke;

import com.lpc.common.PairingProvider;
import com.lpc.crypto.Hash;
import it.unisa.dia.gas.jpbc.Element;

import java.text.SimpleDateFormat;
import java.util.*;

public class Test implements PairingProvider {
    public static void main(String[] args) throws Exception {

    }
//        DTPKE dtpke=new DTPKE();
//        Map<String, Map> dtpkeSetup = dtpke.Setup(512);
//        Map<String,Element>MK= (Map<String, Element>) dtpkeSetup.get("MK");
//        Map<String,Object>EK= (Map<String, Object>) dtpkeSetup.get("EK");
//        Map<String,Object>CK= (Map<String, Object>) dtpkeSetup.get("CK");
//
//        List<String>IDset=new ArrayList<>();
//        //创建10个用户
//        for(int i=0;i<10;i++){
//            IDset.add(Hash.calculateSM3Hash(getCurrentTimestamp()));
//        }
//        //S集合用来保存upk集
//        Element[]S=new Element[10];
//        //usksetj=集合来保存usk集
//        Element[]uskset=new Element[10];
//        Element[]uvkset=new Element[10];
//        for(int i=0;i<10;i++) {
//            Map<String, Element> keys = dtpke.Join(MK, IDset.get(i));
//            S[i]=keys.get("upk").getImmutable();
//            uskset[i]=keys.get("usk").getImmutable();
//            uvkset[i]=keys.get("uvk").getImmutable();
//        }
//
//        int t=5;
//
//        Element[]T=new Element[5];
//        T[0]=S[0];
//        T[1]=S[3];
//        T[2]=S[4];
//        T[3]=S[7];
//        T[4]=S[9];
//        List<Object> ATSsignature = new ArrayList<>();
//        ATSsignature.add(42);
//        ATSsignature.add(true);
//        ATSsignature.add(new int[]{1, 2, 3});
//        ATSsignature.add(Arrays.asList("apple", "banana", "cherry"));
//
//        long l9 = System.currentTimeMillis();
//        List<Object> dtpkeEncrypt= dtpke.Encrypt(EK, S, t, ATSsignature);
//        long l10 = System.currentTimeMillis();
//        long l11=l10-l9;
//        System.out.println("Encrypt1用时为： "+l11+"ms");
//
//        int length=dtpkeEncrypt.size();
//        String encryptedATSsign2= (String) dtpkeEncrypt.get(length-1);
//        Object[]fullHdr=new Object[3];
//
//        Element[]S3=new Element[length-5];
//        for(int i=0;i<length-5;i++){
//            S3[i]=(Element) dtpkeEncrypt.get(i);
//        }
//
//        int t3=(int) dtpkeEncrypt.get(length-5);
//        Element[]Hdr= new Element[2];
//        Hdr[0]=(Element) dtpkeEncrypt.get(length-3);
//        Hdr[1]=(Element) dtpkeEncrypt.get(length-2);
//
//        fullHdr[0]=S3;
//        fullHdr[1]=t3;
//        fullHdr[2]=Hdr;
//
//        long l6 = System.currentTimeMillis();
//        System.out.println("ValidateCT 检验结果： "+dtpke.ValidateCT(EK,S,t,Hdr));
//        long l7 = System.currentTimeMillis();
//        long l8=l7-l6;
//        System.out.println("ValidateCT用时为： "+l8+"ms");
//        List<Element>[]shares=new ArrayList[t3];
//        for(int i=0;i<shares.length;i++){
//            shares[i]=new ArrayList<>();
//        }
//        int[]signersindex={0,3,4,7,9};
//        int count=0;
//        long l12 = System.currentTimeMillis();
//        for(int index:signersindex) {
//            shares[count++]=dtpke.ShareDecrypt(uskset[index],Hdr);
//        }
//        long l13 = System.currentTimeMillis();
//        long l14=l13-l12;
//        System.out.println("每个ShareDecrypt用时为： "+l14/5+"ms");
//        int count2=0;
//        long l2 = System.currentTimeMillis();
//        for(int index:signersindex){
//            System.out.println("下标："+index+"的ShareVerify检验结果为： "+dtpke.ShareVerify(EK,fullHdr,shares[count2++],S3[index]));
//        }
//        long l3 = System.currentTimeMillis();
//        long l4=l3-l2;
//        System.out.println("每个ShareVerify用时为： "+l4/5+"ms");
//
//        long l = System.currentTimeMillis();
//        List<Object> message = dtpke.ShareCombine(CK, fullHdr, T, shares, encryptedATSsign2);
//        long l1 = System.currentTimeMillis();
//        long l5=l1-l;
//        System.out.println("ShareCombine用时为： "+l5+"ms");
//        System.out.println("明文消息为： "+message);
//    }
//
//    public static String getCurrentTimestamp() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//        Date date = new Date();
//        Random random = new Random();
//        return "lpc"+sdf.format(date)+random.nextInt();
//    }
}
