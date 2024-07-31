package com.lpc.kase;

import com.lpc.common.PairingProvider;
import it.unisa.dia.gas.jpbc.Element;
import jnr.ffi.annotations.In;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Tes implements PairingProvider {
    public static void main(String[] args) {
        KASETest kase=new KASETest();
        StringBuilder sb = new StringBuilder();
        Map<String, Object> setup=null;
        for(int i=100;i<=1000;i+=100) {
            System.out.println("i = " + i);
            long l1 = System.currentTimeMillis();
            if(i==200){
                setup=kase.Setup(i);
            }else {
                kase.Setup(i);
            }
            long l2 = System.currentTimeMillis();
            sb.append(i).append(" sigma ").append("Set up uses ").append(l2-l1).append(" ms").append("\n");
        }

        Element[]PubK = (Element[]) setup.get("PubK");
        Map<String, Element> stringElementMap = kase.KeyGen(160, g);

        Element pk=stringElementMap.get("pk").getImmutable();
        Element msk=stringElementMap.get("msk").getImmutable();

        int[]sLengthArr={10,20,30,40,50,60,70,80,90,100,110,120};
        for (int length : sLengthArr) {
            System.out.println("Number of keywords = " + length);
            int[]myS=new int[length];
            List<Map.Entry<Integer,byte[]>> list=new ArrayList<>();
            for(int i=0;i<length;i++) {
                myS[i]=i+1;
                list.add(new AbstractMap.SimpleImmutableEntry(i+1,String.valueOf(i+1).getBytes(StandardCharsets.UTF_8)));
            }
            long l9 = System.currentTimeMillis();
            Element kagg = kase.Extract(msk, myS, PubK);
            long l10 = System.currentTimeMillis();
            long extractTime = l10 - l9;
            System.out.println("extractTime = " + extractTime);
            sb.append(length).append(" ").append("extractTime = ").append(extractTime).append("\n");

            long encryptTime=0;
            List<Map<String, Element>>ciphers=new ArrayList<>();
            for(Map.Entry<Integer,byte[]> entry:list){
                long l11 = System.currentTimeMillis();
                Map<String, Element> cipher = kase.Encrypt(entry.getValue(), entry.getKey(), g, pk, PubK);
                long l12 = System.currentTimeMillis();
                encryptTime += l12 - l11;
                ciphers.add(cipher);
            }
            System.out.println("encryptTime = " + encryptTime);
            sb.append(length).append(" ").append("encryptTime = ").append(encryptTime).append("\n");

            long l7 = System.currentTimeMillis();
            Element trapdoor = kase.Trapdoor(kagg, String.valueOf(length).getBytes(StandardCharsets.UTF_8));
            long l8 = System.currentTimeMillis();
            long trapdoorTime=l8-l7;
            System.out.println("Trapdoor Time= " + trapdoorTime);
            sb.append(length).append(" ").append("Trapdoor Time= ").append(trapdoorTime).append("\n");

            int count=0;
            long adjustTime=0;
            long testTime=0;
            for(Map.Entry<Integer,byte[]> entry:list){
                long l3 = System.currentTimeMillis();
                Element Tri = kase.Adjust(entry.getKey(), myS, trapdoor, PubK);
                long l4 = System.currentTimeMillis();
                adjustTime+=l4 - l3;

                Map<String, Element> cipher = ciphers.get(count++);
                Element cw = cipher.get("cw");
                Element c1 = cipher.get("c1");
                Element c2 = cipher.get("c2");

                long l5 = System.currentTimeMillis();
                boolean test = kase.Test(Tri, myS, cw, c1, c2, PubK);
                long l6 = System.currentTimeMillis();
                testTime+=l6 - l5;

                if(test){
                    System.out.println(true);
                    break;
                }
            }

            System.out.println("adjustTime = " + adjustTime);
            System.out.println("testTime = " + testTime);
            System.out.println("*******************************");
            System.out.println("*******************************");
            sb.append(length).append(" ").append("adjustTime= ").append(adjustTime).append("\n");
            sb.append(length).append(" ").append("testTime = ").append(testTime).append("\n");

            System.out.println();

        }
        System.out.println(sb);

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("output.txt"));){
            bufferedWriter.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
