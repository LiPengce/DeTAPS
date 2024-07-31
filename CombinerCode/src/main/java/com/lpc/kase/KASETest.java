package com.lpc.kase;

import com.lpc.common.PairingProvider;
import com.lpc.crypto.Hash;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KASETest implements PairingProvider{
    public Pairing bp=PairingProvider.pairing;
    public Field G=PairingProvider.G;
    public Field Zr=PairingProvider.Zr;
    private Element g=PairingProvider.g;

    private int n=200;

    public Map<String,Object> Setup(int n){
        Element alpha=Zr.newRandomElement().getImmutable();
        Element[]PubK=new Element[2*n+1];
        PubK[0]=g.getImmutable();

        for(int i=1;i<PubK.length;i++){
            PubK[i]=g.powZn(alpha.powZn(Zr.newElement(i))).getImmutable();
        }
        Map<String, Object>kaseSetup=new HashMap<>();

        kaseSetup.put("PubK",PubK);
        kaseSetup.put("n",n);

        return kaseSetup;
    }

    public Map<String,Element> KeyGen(int lambda,Element g){
        Element gamma=Zr.newRandomElement().getImmutable();
        Element msk=Zr.newElement(gamma).getImmutable();
        Element pk=g.powZn(gamma).getImmutable();
        Element v=G.newElement(pk).getImmutable();
        Map<String,Element>kaseKeyGen=new HashMap<>();
        //v=pk msk=gamma
        kaseKeyGen.put("msk",msk);
        kaseKeyGen.put("gamma",gamma);
        kaseKeyGen.put("pk",pk);
        kaseKeyGen.put("v",v);

        return kaseKeyGen;
    }

    //规定下标从1开始
    public Map<String,Element> Encrypt(byte[]keyword,int i,Element g,Element pk,Element[]PubK){
        Element k=Zr.newRandomElement().getImmutable();
        Element c1=g.powZn(k).getImmutable();
        Element gi=PubK[i];
        Element c2=pk.mul(gi).powZn(k).getImmutable();
        Element g1=PubK[1].getImmutable();
        Element gn=PubK[n].getImmutable();

        byte[] wbytes = Hash.calculateSM3Hash(keyword);
        Element w = G.newElementFromHash(wbytes, 0, wbytes.length).getImmutable();
        Element cw=bp.pairing(g,w).div(pairing.pairing(g1, gn)).powZn(k).getImmutable();

        Map<String,Element>kase_Enc=new HashMap<>();
        kase_Enc.put("c1",c1);
        kase_Enc.put("c2",c2);
        kase_Enc.put("cw",cw);


        return kase_Enc;
    }

    public Element Extract(Element msk,int[]S,Element[]PubK){
        Element kagg = G.newOneElement();
        Element tmp;
        for(int j:S){
            tmp=PubK[this.n+1-j].getImmutable();
            kagg.mul(tmp.powZn(msk).getImmutable());
        }
        return kagg.getImmutable();
    }

    public Element Trapdoor(Element kagg,byte[]keyword){
        byte[] wbytes = Hash.calculateSM3Hash(keyword);
        Element w = G.newElementFromHash(wbytes, 0, wbytes.length).getImmutable();
        Element Tr=kagg.mul(w).getImmutable();
        return Tr.getImmutable();
    }

    public Element Adjust(int i,int[]S,Element Tr,Element[]Pubk){
        Element Tri=G.newOneElement();
        for(int j:S){
            if(j!=i) {
                Tri.mul(Pubk[n+1-j+i]);
            }
        }
        Tri.mul(Tr).getImmutable();
        return Tri.getImmutable();
    }

    public boolean Test(Element Tri,int[]S,Element cw,Element c1,Element c2,Element[]PubK){
        Element pub=G.newOneElement();

        for(int j:S){
            pub.mul(PubK[n+1-j]);
        }
        return cw.equals(bp.pairing(Tri,c1).div(bp.pairing(pub.duplicate(),c2)));
    }

}
