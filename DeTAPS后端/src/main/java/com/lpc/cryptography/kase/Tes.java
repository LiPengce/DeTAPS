package com.lpc.cryptography.kase;

import com.lpc.cryptography.common.PairingProvider;
import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Tes implements PairingProvider {
    public static void main(String[] args) {
        KASE2 kase=new KASE2();
        Map<String, Object> setup = kase.Setup(160);
        Element[]PubK = (Element[]) setup.get("PubK");
        Map<String, Element> stringElementMap = kase.KeyGen(160, g);
        Element x1=G.newRandomElement().getImmutable();
        Element x2=G.newRandomElement().getImmutable();
        Element x3=G.newRandomElement().getImmutable();
        List<byte[]>keywords=new ArrayList<>();
        keywords.add(x1.toBytes());
        keywords.add(x2.toBytes());
        keywords.add(x3.toBytes());
        int i=2;
        Element pk=stringElementMap.get("pk").getImmutable();
        Element msk=stringElementMap.get("msk").getImmutable();
        Map<String, Object> encrypt = kase.Encrypt(keywords, i, g, pk, PubK);
        List<Element>cws= (List<Element>) encrypt.get("cws");
        Element c1= ((Element) encrypt.get("c1")).getImmutable();
        Element c2= ((Element) encrypt.get("c2")).getImmutable();
        int[]S={1,2,3,4,5};
        Element kagg = kase.Extract(msk, S, PubK);
        Element trapdoor1 = kase.Trapdoor(kagg, x1.toBytes());
        for(int index:S){
            Element Tri = kase.Adjust(index, S, trapdoor1, PubK);
            System.out.println(kase.Test(Tri, S, cws, c1, c2, PubK));
        }
    }
}
