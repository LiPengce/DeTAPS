package com.lpc.detaps;

import com.lpc.common.PairingProvider;
import com.lpc.crypto.Hash;
import it.unisa.dia.gas.jpbc.Element;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"all"})
public class ZeroKnowledgeProof implements PairingProvider {
    public static List<Element> Proof1_1(int[] b_set , List<Element> specN, List<Element>upkSet) {
        int n3 = upkSet.size();
        Element[] alphaSet = new Element[n3];
        Element[] alphaPlusSet = new Element[n3];
        for (int i = 0; i < alphaSet.length; i++) {
            alphaSet[i] = Zr.newRandomElement().getImmutable();
        }
        //计算B
        Element B = G.newRandomElement().getImmutable();
        for (int i = 0; i < alphaSet.length; i++) {
            B = B.mul(upkSet.get(i).powZn(alphaSet[i])).getImmutable();
        }
        Element V2=G.newOneElement().getImmutable();
        for(int i=0;i< b_set.length;i++){
            V2=V2.mul(upkSet.get(i).powZn(Zr.newElement(b_set[i]))).getImmutable();
        }
        StringBuilder Hsb = new StringBuilder();
        for (Element upk : upkSet) {
            Hsb.append(upk);
        }
        Hsb.append(V2);
        Hsb.append(B);
        byte[] Hbytes = Hsb.toString().getBytes(StandardCharsets.UTF_8);
        byte[]Hhash=Hash.calculateSM3Hash(Hbytes);
        Element H = Zr.newElementFromHash(Hhash, 0, Hhash.length).getImmutable();
        for (int i = 0; i < alphaPlusSet.length; i++) {
            alphaPlusSet[i] = Zr.newElement(b_set[i]).mul(H).add(alphaSet[i]).getImmutable();
        }
        List<Element> proof1_1 = new ArrayList<>();
        //B,alphaPlusSet
        proof1_1.add(B);
        for (Element alphaPlus : alphaPlusSet) {
            proof1_1.add(alphaPlus);
        }
        return proof1_1;
    }

    public static boolean Verify1_1(List<Element> proof1_1, List<Element> upkSet, Element V) {
        Element B = proof1_1.get(0).getImmutable();
        Element[] alphaPlusSet = new Element[proof1_1.size() - 1];
        if (alphaPlusSet.length != upkSet.size()) {
            System.out.println("Verify1_1中长度出现错误！");
            System.exit(-1);
        }
        for (int i = 1; i < proof1_1.size(); i++) {
            alphaPlusSet[i - 1] = proof1_1.get(i).getImmutable();
        }
        StringBuilder Hstr = new StringBuilder();
        for (Element upk : upkSet) {
            Hstr.append(upk);
        }
        Hstr.append(V);
        Hstr.append(B);
        byte[] Hbytes = Hstr.toString().getBytes(StandardCharsets.UTF_8);
        Element H = Zr.newElementFromHash(Hbytes, 0, Hbytes.length).getImmutable();
        Element left = V.powZn(H).mul(B).getImmutable();
        Element right = G.newOneElement();
        for (int i = 0; i < upkSet.size(); i++) {
            right.mul(upkSet.get(i).powZn(alphaPlusSet[i]));
        }
        return left.equals(right);
    }

    public static List<Element> Proof1_2_1(Element V0, Element psi1, Element g) {
        Element alpha = Zr.newRandomElement().getImmutable();
        Element B = g.powZn(alpha).getImmutable();
        StringBuilder sb = new StringBuilder();
        sb.append(g);
        sb.append(V0);
        sb.append(B);
        byte[] Hbytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        Element H = Zr.newElementFromHash(Hbytes, 0, Hbytes.length).getImmutable();
        Element alphaPlus = psi1.mul(H).add(alpha).getImmutable();
        //g,V0,B,alphaPlus
        List<Element> proof1_2_1 = new ArrayList<>();
        proof1_2_1.add(g);
        proof1_2_1.add(V0);
        proof1_2_1.add(B);
        proof1_2_1.add(alphaPlus);

        return proof1_2_1;
    }

    public static boolean Verify1_2_1(List<Element> proof1_2_1) {
        Element g = proof1_2_1.get(0).getImmutable();
        Element V0 = proof1_2_1.get(1).getImmutable();
        Element B = proof1_2_1.get(2).getImmutable();
        Element alphaPlus = proof1_2_1.get(3).getImmutable();
        StringBuilder sb = new StringBuilder();
        sb.append(proof1_2_1.get(0));
        sb.append(proof1_2_1.get(1));
        sb.append(proof1_2_1.get(2));
        byte[] Hbytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        Element H = Zr.newElementFromHash(Hbytes, 0, Hbytes.length).getImmutable();
        Element left = V0.powZn(H).mul(B).getImmutable();
        Element right = g.powZn(alphaPlus).getImmutable();

        return left.equals(right);
    }

    //(B,v1,g,h,aPlus1-aPlusn,aPlusn+1)
    public static List<Element> Proof1_2_2(Element V1, Element g, Element h, int[] bSet, Element psi1) {
        int n3 = bSet.length;
        Element[] alphaSet = new Element[n3 + 1];
        for(int i=0;i<alphaSet.length;i++){
            alphaSet[i]=Zr.newRandomElement().getImmutable();
        }
        Element B = h.powZn(alphaSet[alphaSet.length - 1]).getImmutable();
        for (int i = 0; i < n3; i++) {
            B = B.mul(g.powZn(alphaSet[i])).getImmutable();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(g);
        sb.append(h);
        sb.append(V1);
        sb.append(B);
        byte[] Hbytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        Element H = Zr.newElementFromHash(Hbytes, 0, Hbytes.length).getImmutable();
        List<Element> proof1_2_2 = new ArrayList<>();
        //g,h,B,V1,alphaPlusSet
        proof1_2_2.add(g);
        proof1_2_2.add(h);
        proof1_2_2.add(B);
        proof1_2_2.add(V1);
        for (int i = 0; i < alphaSet.length - 1; i++) {
            proof1_2_2.add(Zr.newElement(bSet[i]).mul(H).add(alphaSet[i]).getImmutable());
        }
        proof1_2_2.add(psi1.mul(H).add(alphaSet[alphaSet.length - 1]).getImmutable());

        return proof1_2_2;
    }

    public static boolean Verify1_2_2(List<Element> proof1_2_2) {
        Element g = proof1_2_2.get(0).getImmutable();
        Element h = proof1_2_2.get(1).getImmutable();
        Element B = proof1_2_2.get(2).getImmutable();
        Element V1 = proof1_2_2.get(3).getImmutable();
        StringBuilder sb = new StringBuilder();
        sb.append(g);
        sb.append(h);
        sb.append(V1);
        sb.append(B);
        byte[] Hbytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        Element H = Zr.newElementFromHash(Hbytes, 0, Hbytes.length).getImmutable();
        Element left = V1.powZn(H).mul(B).getImmutable();
        //得到alphaPlus集合
        Element[] alphaPlusSet = new Element[proof1_2_2.size() - 4];
        if (alphaPlusSet.length != proof1_2_2.size() - 4) {
            System.out.println("Verify1_2_2中长度出现错误！");
            System.exit(-1);
        }
        for (int i = 0; i < alphaPlusSet.length; i++) {
            alphaPlusSet[i] = proof1_2_2.get(i + 4).getImmutable();
        }
        int alphaPlusSetlength = alphaPlusSet.length;
        Element right = h.powZn(alphaPlusSet[alphaPlusSetlength - 1]).getImmutable();
        for (int i = 0; i < alphaPlusSetlength - 1; i++) {
            right=right.mul(g.powZn(alphaPlusSet[i])).getImmutable();
        }

        return left.equals(right);
    }
    //方法注释：Hi(pp, x, a1, c1, . . . , ci−1, ai)
    public static List<Element> Proof1_3(Element g,Element h,int[] bSet,List<Element>hList) {
        //h1~hn,v0~vn,challenge1:alpha,(Sa,SbSet,Sc),challenge2:beta,gammaPlus,bPlusSet,phiPlusSet
        List<Element>proof1_3=new ArrayList<>();
        int n = bSet.length;
        Element gamma = Zr.newRandomElement().getImmutable();
        for(int i=0;i<hList.size();i++){
            proof1_3.add(hList.get(i));
        }
        StringBuilder publicParams=new StringBuilder();
        //公共参数包括g,h,pk1~pkn
        publicParams.append(g);
        publicParams.append(h);

        Element[] vSet = new Element[n + 1];
        vSet[0] = g.powZn(gamma).getImmutable();
        for (int i = 1; i < vSet.length; i++) {
            vSet[i] = g.powZn(Zr.newElement(bSet[i - 1])).mul(hList.get(i-1).powZn(gamma)).getImmutable();
        }

        //第一轮的证明信息st1
        StringBuilder st1 = new StringBuilder();
        for(int i=0;i<hList.size();i++){
            st1.append(hList.get(i));
        }
        for (int i = 0; i < vSet.length; i++) {
            st1.append(vSet[i]);
            proof1_3.add(vSet[i]);
        }
        //challenge1
        StringBuilder challenge1=publicParams.append(st1);
        //challenge1:alpha
        byte[] alphaBytes = challenge1.toString().getBytes(StandardCharsets.UTF_8);
        byte[]alphaHash=Hash.calculateSM3Hash(alphaBytes);
        Element alpha = Zr.newElementFromHash(alphaHash, 0, alphaHash.length).getImmutable();
        //pi添加challange1
        proof1_3.add(alpha);
        Element[] phiSet = new Element[n];
        for (int i = 0; i < phiSet.length; i++) {
            phiSet[i] = alpha.powZn(Zr.newElement(i + 1)).mul(gamma).mul(Zr.newElement(1 - bSet[i])).getImmutable();
        }

        Element kgamma = Zr.newRandomElement().getImmutable();
        Element[] kbSet = new Element[n];
        for (int i = 0; i < kbSet.length; i++) {
            kbSet[i] = Zr.newRandomElement().getImmutable();
        }
        Element[] kphiSet = new Element[n];
        for (int i = 0; i < kphiSet.length; i++) {
            kphiSet[i] = Zr.newRandomElement().getImmutable();
        }

        Element Sa = g.powZn(kgamma).getImmutable();
        proof1_3.add(Sa);
        Element[] Sbset = new Element[n];
        for (int i = 0; i < Sbset.length; i++) {
            Sbset[i] = g.powZn(kbSet[i]).mul(hList.get(i).powZn(kgamma)).getImmutable();
            //pi添加第二轮证明结果
            proof1_3.add(Sbset[i]);
        }
        Element Sc = G.newOneElement().getImmutable();
        for (int i = 0; i < n; i++) {
            Sc=Sc.mul(vSet[i + 1].powZn(alpha.powZn(Zr.newElement(i + 1)).mul(kbSet[i])).mul(hList.get(i).powZn(kphiSet[i]))).getImmutable();
        }
        proof1_3.add(Sc);
        //第二轮的证明信息st2
        StringBuilder st2=new StringBuilder();
        st2.append(Sa);
        for(Element Sbi:Sbset){
            st2.append(Sbi);
        }
        st2.append(Sc);
        //challenge2,包括st1,challange1:alpha,st2
        StringBuilder challenge2=publicParams.append(st1).append(alpha).append(st2);
        byte[] betaBytes = challenge2.toString().getBytes(StandardCharsets.UTF_8);
        byte[] betahash = Hash.calculateSM3Hash(betaBytes);
        //challenge2:beta
        Element beta = Zr.newElementFromHash(betahash,0,betahash.length).getImmutable();
        //pi添加challenge2
        proof1_3.add(beta);
        Element gammaPlus = gamma.mul(beta).add(kgamma).getImmutable();
        //添加gammaPlus
        proof1_3.add(gammaPlus);
        Element[] bPlusSet = new Element[n];
        for (int i = 0; i < bPlusSet.length; i++) {
            bPlusSet[i] = Zr.newElement(bSet[i]).mul(beta).add(kbSet[i]).getImmutable();
            proof1_3.add(bPlusSet[i]);
        }
        Element[] phiPlusSet = new Element[n];
        for (int i = 0; i < phiPlusSet.length; i++) {
            phiPlusSet[i] = phiSet[i].mul(beta).add(kphiSet[i]).getImmutable();
            proof1_3.add(phiPlusSet[i]);
        }

        return proof1_3;

    }
    public static boolean Verify1_3(Element g,Element h,List<Element>proof1_3){
        //h1~hn,v0~vn,challenge1:alpha,(Sa,SbSet,Sc),challenge2:beta,gammaPlus,bPlusSet,phiPlusSet,
        //n      n+1            1        1   n    1            1          1         n      n  =5n+6个元素
        int n=(proof1_3.size()-6)/5;
        Element[]hSet=new Element[n];
        for(int i=0;i<n;i++){
            hSet[i]=proof1_3.get(i).getImmutable();
        }
        Element[]vSet=new Element[n+1];
        for(int i=0;i< vSet.length;i++){
            vSet[i]=proof1_3.get(i+n).getImmutable();
        }
        Element orgalpha=proof1_3.get(2*n+1).getImmutable();
        Element Sa=proof1_3.get(2*n+2);
        Element[]Sbset=new Element[n];
        for(int i=0;i< Sbset.length;i++){
            Sbset[i]=proof1_3.get(i+2*n+3).getImmutable();
        }
        Element Sc=proof1_3.get(3*n+3).getImmutable();
        Element orgbeta=proof1_3.get(3*n+4).getImmutable();
        Element gammaPlus=proof1_3.get(3*n+5).getImmutable();
        Element[]bPlusSet=new Element[n];
        for(int i=0;i< bPlusSet.length;i++){
            bPlusSet[i]=proof1_3.get(i+3*n+6).getImmutable();
        }
        Element[]phiPlusSet=new Element[n];
        for(int i=0;i<phiPlusSet.length;i++){
            phiPlusSet[i]=proof1_3.get(i+4*n+6).getImmutable();
        }

        //公共参数包括g，hR,c,pk1~pkn
        StringBuilder publicParams=new StringBuilder();
        publicParams.append(g);
        publicParams.append(h);

        //第一轮的证明信息st1
        StringBuilder st1 = new StringBuilder();
        for(int i=0;i<hSet.length;i++){
            st1.append(hSet[i]);
        }
        for (int i = 0; i < vSet.length; i++) {
            st1.append(vSet[i]);
        }
        //challenge1
        StringBuilder challenge1=publicParams.append(st1);
        //challenge1:alpha
        byte[] alphaBytes = challenge1.toString().getBytes(StandardCharsets.UTF_8);
        byte[] alphaHash = Hash.calculateSM3Hash(alphaBytes);
        Element alpha = Zr.newElementFromHash(alphaHash, 0, alphaHash.length).getImmutable();
        if(!alpha.equals(orgalpha)){
            System.out.println("first challenge fail!");
            return false;
        }

        //第二轮的证明信息st2
        StringBuilder st2=new StringBuilder();
        st2.append(Sa);
        for(Element Sbi:Sbset){
            st2.append(Sbi);
        }
        st2.append(Sc);
        //challenge2,包括st1,challange1:alpha,st2
        StringBuilder challenge2=publicParams.append(st1).append(alpha).append(st2);
        byte[] betaBytes = challenge2.toString().getBytes(StandardCharsets.UTF_8);
        //challenge2:beta
        byte[] betaHash = Hash.calculateSM3Hash(betaBytes);
        Element beta = Zr.newElementFromHash(betaHash,0,betaHash.length).getImmutable();
        if(!beta.equals(orgbeta)){
            System.out.println("second challenge fail!");
            return false;
        }
        //开始验证
        Element v0=vSet[0].getImmutable();
        Element left1=Sa.mul(v0.powZn(beta)).getImmutable();
        Element right1=g.powZn(gammaPlus).getImmutable();
        if(!left1.equals(right1)){
            System.out.println("first equation verification fail!");
            return false;
        }
        for(int i=0;i< Sbset.length;i++){
            Element left2=Sbset[i].mul(vSet[i+1].powZn(beta)).getImmutable();
            Element right=g.powZn(bPlusSet[i]).mul(hSet[i].powZn(gammaPlus)).getImmutable();
            if(!left2.equals(right)){
                System.out.println("second equation verification fail!");
                return false;
            }
        }
        Element left3=G.newElement(Sc);
        Element right3=G.newOneElement();
        for(int i=0;i< bPlusSet.length;i++){
            left3.mul(vSet[i+1].powZn(beta.mul(alpha.powZn(Zr.newElement(i+1)))));
            right3.mul(vSet[i+1].powZn(alpha.powZn(Zr.newElement(i+1)).mul(bPlusSet[i])).mul(hSet[i].powZn(phiPlusSet[i])));
        }
        if(!left3.equals(right3)){
            System.out.println("third equation verification fail!");
            return false;
        }
        return true;
    }

    public static List<Element> Proof2_1(Element g, Element R, Element z, Element c, List<Element> pk, int[] bSet) {
        int n = bSet.length;
        if (pk.size() != n) {
            System.out.println("pk的长度不是n！");
            System.exit(-1);
        }
        Element kz = Zr.newRandomElement().getImmutable();
        Element[] kbSet = new Element[n];
        for (int i = 0; i < n; i++) {
            kbSet[i] = Zr.newRandomElement().getImmutable();
        }
        Element A = g.powZn(kz).getImmutable();
        for (int i = 0; i < n; i++) {
            A = A.mul(pk.get(i).powZn(Zr.newZeroElement().sub(c.mul(kbSet[i])))).getImmutable();
        }
        Element r = Zr.newRandomElement().getImmutable();
        Element B = g.powZn(z.add(r)).getImmutable();
        Element zPlus = z.add(r).getImmutable();
        Element Rplus = R.mul(g.powZn(r)).getImmutable();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pk.size(); i++) {
            sb.append(pk.get(i));
        }
        sb.append(c); //0
        sb.append(A); //1
        sb.append(B); //2
        sb.append(zPlus); //3
        sb.append(Rplus); //4
        byte[] Hbytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        byte[]Hhash=Hash.calculateSM3Hash(Hbytes);
        Element H = Zr.newElementFromHash(Hhash, 0, Hhash.length).getImmutable();
        Element zPPlus = zPlus.mul(H).add(kz).getImmutable();
        Element[] bPlusSet = new Element[n];
        for (int i = 0; i < bPlusSet.length; i++) {
            bPlusSet[i] = Zr.newElement(bSet[i]).mul(H).add(kbSet[i]).getImmutable();
        }

        List<Element> proof2_1 = new ArrayList<>();
        //g,c,A,B,zPlus,Rplus,zPPlus,bPlusSet
        proof2_1.add(g);  //0
        proof2_1.add(c);  //1
        proof2_1.add(A);  //2
        proof2_1.add(B);  //3
        proof2_1.add(zPlus); //4
        proof2_1.add(Rplus); //5
        proof2_1.add(zPPlus);//6
        for (Element bPlusi : bPlusSet) {
            proof2_1.add(bPlusi);
        }

        return proof2_1;
    }

    public static boolean Verify2_1(List<Element> proof2_1, List<Element> pk) {
        Element g = proof2_1.get(0).getImmutable();
        Element c = proof2_1.get(1).getImmutable();
        Element A = proof2_1.get(2).getImmutable();
        Element B =  proof2_1.get(3).getImmutable();
        Element zPlus=proof2_1.get(4).getImmutable();
        Element Rplus = proof2_1.get(5).getImmutable();
        Element zPPlus = proof2_1.get(6).getImmutable();
        int proof2_1length = proof2_1.size();
        Element[] bPlusSet = new Element[proof2_1length - 7];
        if (bPlusSet.length != pk.size()) {
            System.out.println("Verify2_1中长度出现错误！");
        }
        for (int i = 0; i < bPlusSet.length; i++) {
            bPlusSet[i] = proof2_1.get(i + 7).getImmutable();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pk.size(); i++) {
            sb.append(pk.get(i));
        }
        sb.append(c);
        sb.append(A);
        sb.append(B);
        sb.append(zPlus);
        sb.append(Rplus);
        byte[] Hbytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        byte[]Hhash=Hash.calculateSM3Hash(Hbytes);
        Element H = Zr.newElementFromHash(Hhash, 0, Hhash.length).getImmutable();
        Element left = A.mul(Rplus.powZn(H)).getImmutable();
        Element tmp = G.newOneElement().getImmutable();
        for (int i = 0; i < pk.size(); i++) {
            tmp=tmp.mul(pk.get(i).powZn(bPlusSet[i])).getImmutable();
        }
        left=left.mul(tmp.powZn(c)).getImmutable();
        Element right = g.powZn(zPPlus).getImmutable();

        return left.equals(right);
    }

    public static List<Element> Proof2_2_1(Element T0, Element psi2, Element g) {
        Element alpha = Zr.newRandomElement().getImmutable();
        Element B = g.powZn(alpha).getImmutable();
        StringBuilder sb = new StringBuilder();
        sb.append(g);
        sb.append(T0);
        sb.append(B);
        byte[] Hbytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        Element H = Zr.newElementFromHash(Hbytes, 0, Hbytes.length).getImmutable();
        Element alphaPlus = psi2.mul(H).add(alpha).getImmutable();

        List<Element> proof2_2 = new ArrayList<>();
        //g,T0,B,alphaPlus
        proof2_2.add((g));
        proof2_2.add((T0));
        proof2_2.add((B));
        proof2_2.add((alphaPlus));

        return proof2_2;
    }

    public static boolean Verify2_2_1(List<Element> proof2_2_1) {
        Element g = proof2_2_1.get(0).getImmutable();
        Element T0 = proof2_2_1.get(1).getImmutable();
        Element B = proof2_2_1.get(2).getImmutable();
        Element alphaPlus = ((proof2_2_1.get(3))).getImmutable();
        StringBuilder sb = new StringBuilder();
        sb.append(proof2_2_1.get(0));
        sb.append(proof2_2_1.get(1));
        sb.append(proof2_2_1.get(2));
        byte[] Hbytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        Element H = Zr.newElementFromHash(Hbytes, 0, Hbytes.length).getImmutable();

        Element left = T0.powZn(H).mul(B).getImmutable();
        Element right = g.powZn(alphaPlus).getImmutable();

        return left.equals(right);
    }

    public static List<Element> Proof2_2_2(Element T1, Element g, Element h, Element psi2, int[] bSet) {
        int n = bSet.length;
        Element[] alphaSet = new Element[n + 1];
        for (int i = 0; i < alphaSet.length; i++) {
            alphaSet[i] = Zr.newRandomElement().getImmutable();
        }
        Element B = h.powZn(alphaSet[alphaSet.length - 1]).getImmutable();
        for (int i = 0; i < alphaSet.length - 1; i++) {
            B = B.mul(g.powZn(alphaSet[i])).getImmutable();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(g);
        sb.append(h);
        sb.append(T1);
        sb.append(B);
        byte[] Hbytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        Element H = Zr.newElementFromHash(Hbytes, 0, Hbytes.length).getImmutable();
        List<Element> proof2_2_2 = new ArrayList<>();
        //g,h,B,T1,alphaPlusSet
        proof2_2_2.add(g);
        proof2_2_2.add(h);
        proof2_2_2.add(B);
        proof2_2_2.add(T1);
        for (int i = 0; i < alphaSet.length - 1; i++) {
            proof2_2_2.add(Zr.newElement(bSet[i]).mul(H).add(alphaSet[i]).getImmutable());
        }
        proof2_2_2.add(psi2.mul(H).add(alphaSet[alphaSet.length - 1]).getImmutable());

        return proof2_2_2;
    }

    public static boolean Verify2_2_2(List<Element> proof2_2_2) {
        Element g = proof2_2_2.get(0).getImmutable();
        Element h = proof2_2_2.get(1).getImmutable();
        Element B = proof2_2_2.get(2).getImmutable();
        Element T1 = proof2_2_2.get(3).getImmutable();
        StringBuilder sb = new StringBuilder();
        sb.append(proof2_2_2.get(0));
        sb.append(proof2_2_2.get(1));
        sb.append(proof2_2_2.get(2));
        sb.append(proof2_2_2.get(3));
        byte[] Hbytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        Element H = Zr.newElementFromHash(Hbytes, 0, Hbytes.length).getImmutable();
        Element[] alphaPlusSet = new Element[proof2_2_2.size() - 4];
        for (int i = 0; i < alphaPlusSet.length; i++) {
            alphaPlusSet[i] = proof2_2_2.get(i + 4).getImmutable();
        }
        Element left = T1.powZn(H).mul(B).getImmutable();
        Element right = h.powZn(alphaPlusSet[alphaPlusSet.length - 1]);
        for (int i = 0; i < alphaPlusSet.length - 1; i++) {
            right=right.mul(g.powZn(alphaPlusSet[i])).getImmutable();
        }

        return left.equals(right);
    }

    public static List<Element> Proof2_3(Element g,Element h,int[] bSet) {
        //h1~hn,v0~vn,challenge1:alpha,(Sa,SbSet,Sc),challenge2:beta,gammaPlus,bPlusSet,phiPlusSet
        List<Element>proof2_3=new ArrayList<>();
        int n = bSet.length;
        Element gamma = Zr.newRandomElement().getImmutable();
        Element[]hSet=new Element[n];
        for(int i=0;i<hSet.length;i++){
            hSet[i]=G.newRandomElement().getImmutable();
            proof2_3.add(hSet[i]);
        }
        StringBuilder publicParams=new StringBuilder();
        //公共参数包括g,h,pk1~pkn
        publicParams.append(g);
        publicParams.append(h);
        Element[] vSet = new Element[n + 1];
        vSet[0] = g.powZn(gamma).getImmutable();
        for (int i = 1; i < vSet.length; i++) {
            vSet[i] = g.powZn(Zr.newElement(bSet[i - 1])).mul(hSet[i - 1].powZn(gamma)).getImmutable();
        }

        //第一轮的证明信息st1
        StringBuilder st1 = new StringBuilder();
        for(int i=0;i< hSet.length;i++){
            st1.append(hSet[i]);
        }
        for (int i = 0; i < vSet.length; i++) {
            st1.append(vSet[i]);
            proof2_3.add(vSet[i]);
        }
        //challenge1
        StringBuilder challenge1=publicParams.append(st1);
        //challenge1:alpha
        byte[] alphaBytes = challenge1.toString().getBytes(StandardCharsets.UTF_8);
        byte[]alphaHash=Hash.calculateSM3Hash(alphaBytes);
        Element alpha = Zr.newElementFromHash(alphaHash, 0, alphaHash.length).getImmutable();
        //pi添加challange1
        proof2_3.add(alpha);
        Element[] phiSet = new Element[n];
        for (int i = 0; i < phiSet.length; i++) {
            phiSet[i] = alpha.powZn(Zr.newElement(i + 1)).mul(gamma).mul(Zr.newElement(1 - bSet[i])).getImmutable();
        }

        Element kgamma = Zr.newRandomElement().getImmutable();
        Element[] kbSet = new Element[n];
        for (int i = 0; i < kbSet.length; i++) {
            kbSet[i] = Zr.newRandomElement().getImmutable();
        }
        Element[] kphiSet = new Element[n];
        for (int i = 0; i < kphiSet.length; i++) {
            kphiSet[i] = Zr.newRandomElement().getImmutable();
        }

        Element Sa = g.powZn(kgamma).getImmutable();
        proof2_3.add(Sa);
        Element[] Sbset = new Element[n];
        for (int i = 0; i < Sbset.length; i++) {
            Sbset[i] = g.powZn(kbSet[i]).mul(hSet[i].powZn(kgamma)).getImmutable();
            //pi添加第二轮证明结果
            proof2_3.add(Sbset[i]);
        }
        Element Sc = G.newOneElement().getImmutable();
        for (int i = 0; i < n; i++) {
            Sc=Sc.mul(vSet[i + 1].powZn(alpha.powZn(Zr.newElement(i + 1)).mul(kbSet[i])).mul(hSet[i].powZn(kphiSet[i]))).getImmutable();
        }
        proof2_3.add(Sc);
        //第二轮的证明信息st2
        StringBuilder st2=new StringBuilder();
        st2.append(Sa);
        for(Element Sbi:Sbset){
            st2.append(Sbi);
        }
        st2.append(Sc);
        //challenge2,包括st1,challange1:alpha,st2
        StringBuilder challenge2=publicParams.append(st1).append(alpha).append(st2);
        byte[] betaBytes = challenge2.toString().getBytes(StandardCharsets.UTF_8);
        byte[] betahash = Hash.calculateSM3Hash(betaBytes);
        //challenge2:beta
        Element beta = Zr.newElementFromHash(betahash,0,betahash.length).getImmutable();
        //pi添加challenge2
        proof2_3.add(beta);
        Element gammaPlus = gamma.mul(beta).add(kgamma).getImmutable();
        //添加gammaPlus
        proof2_3.add(gammaPlus);
        Element[] bPlusSet = new Element[n];
        for (int i = 0; i < bPlusSet.length; i++) {
            bPlusSet[i] = Zr.newElement(bSet[i]).mul(beta).add(kbSet[i]).getImmutable();
            proof2_3.add(bPlusSet[i]);
        }
        Element[] phiPlusSet = new Element[n];
        for (int i = 0; i < phiPlusSet.length; i++) {
            phiPlusSet[i] = phiSet[i].mul(beta).add(kphiSet[i]).getImmutable();
            proof2_3.add(phiPlusSet[i]);
        }

        return proof2_3;
    }

    public static boolean Verify2_3(Element g,Element h,List<Element>proof2_3){
        //h1~hn,v0~vn,challenge1:alpha,(Sa,SbSet,Sc),challenge2:beta,gammaPlus,bPlusSet,phiPlusSet,
        //n      n+1            1        1   n    1            1          1         n      n  =5n+6个元素
        int n=(proof2_3.size()-6)/5;
        Element[]hSet=new Element[n];
        for(int i=0;i<n;i++){
            hSet[i]=proof2_3.get(i).getImmutable();
        }
        Element[]vSet=new Element[n+1];
        for(int i=0;i< vSet.length;i++){
            vSet[i]=proof2_3.get(i+n).getImmutable();
        }
        Element orgalpha=proof2_3.get(2*n+1).getImmutable();
        Element Sa=proof2_3.get(2*n+2);
        Element[]Sbset=new Element[n];
        for(int i=0;i< Sbset.length;i++){
            Sbset[i]=proof2_3.get(i+2*n+3).getImmutable();
        }
        Element Sc=proof2_3.get(3*n+3).getImmutable();
        Element orgbeta=proof2_3.get(3*n+4).getImmutable();
        Element gammaPlus=proof2_3.get(3*n+5).getImmutable();
        Element[]bPlusSet=new Element[n];
        for(int i=0;i< bPlusSet.length;i++){
            bPlusSet[i]=proof2_3.get(i+3*n+6).getImmutable();
        }
        Element[]phiPlusSet=new Element[n];
        for(int i=0;i<phiPlusSet.length;i++){
            phiPlusSet[i]=proof2_3.get(i+4*n+6).getImmutable();
        }

        //公共参数包括g，hR,c,pk1~pkn
        StringBuilder publicParams=new StringBuilder();
        publicParams.append(g);
        publicParams.append(h);

        //第一轮的证明信息st1
        StringBuilder st1 = new StringBuilder();
        for(int i=0;i<hSet.length;i++){
            st1.append(hSet[i]);
        }
        for (int i = 0; i < vSet.length; i++) {
            st1.append(vSet[i]);
        }
        //challenge1
        StringBuilder challenge1=publicParams.append(st1);
        //challenge1:alpha
        byte[] alphaBytes = challenge1.toString().getBytes(StandardCharsets.UTF_8);
        byte[] alphaHash = Hash.calculateSM3Hash(alphaBytes);
        Element alpha = Zr.newElementFromHash(alphaHash, 0, alphaHash.length).getImmutable();
        if(!alpha.equals(orgalpha)){
            System.out.println("first challenge fail!");
            return false;
        }

        //第二轮的证明信息st2
        StringBuilder st2=new StringBuilder();
        st2.append(Sa);
        for(Element Sbi:Sbset){
            st2.append(Sbi);
        }
        st2.append(Sc);
        //challenge2,包括st1,challange1:alpha,st2
        StringBuilder challenge2=publicParams.append(st1).append(alpha).append(st2);
        byte[] betaBytes = challenge2.toString().getBytes(StandardCharsets.UTF_8);
        //challenge2:beta
        byte[] betaHash = Hash.calculateSM3Hash(betaBytes);
        Element beta = Zr.newElementFromHash(betaHash,0,betaHash.length).getImmutable();
        if(!beta.equals(orgbeta)){
            System.out.println("second challenge fail!");
            return false;
        }
        //开始验证
        Element v0=vSet[0].getImmutable();
        Element left1=Sa.mul(v0.powZn(beta)).getImmutable();
        Element right1=g.powZn(gammaPlus).getImmutable();
        if(!left1.equals(right1)){
            System.out.println("first equation verification fail!");
            return false;
        }
        for(int i=0;i< Sbset.length;i++){
            Element left2=Sbset[i].mul(vSet[i+1].powZn(beta)).getImmutable();
            Element right=g.powZn(bPlusSet[i]).mul(hSet[i].powZn(gammaPlus)).getImmutable();
            if(!left2.equals(right)){
                System.out.println("second equation verification fail!");
                return false;
            }
        }
        Element left3=G.newElement(Sc);
        Element right3=G.newOneElement();
        for(int i=0;i< bPlusSet.length;i++){
            left3.mul(vSet[i+1].powZn(beta.mul(alpha.powZn(Zr.newElement(i+1)))));
            right3.mul(vSet[i+1].powZn(alpha.powZn(Zr.newElement(i+1)).mul(bPlusSet[i])).mul(hSet[i].powZn(phiPlusSet[i])));
        }
        if(!left3.equals(right3)){
            System.out.println("third equation verification fail!");
            return false;
        }

        return true;
    }

    //这里的x是pk转换成Element的结果
    public static List<Element> Proof3(Element g, Element h, Element x, Element compk, Element rpk) {
        Element alpha1 = Zr.newRandomElement().getImmutable();
        Element alpha2 = Zr.newRandomElement().getImmutable();
        Element B = g.powZn(alpha1).mul(h.powZn(alpha2)).getImmutable();
        StringBuilder sb = new StringBuilder();
        sb.append(g);
        sb.append(h);
        sb.append(compk);
        sb.append(B);
        byte[] Hbytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        Element H = Zr.newElementFromHash(Hbytes, 0, Hbytes.length).getImmutable();
        Element alpha1Plus = H.mul(x).add(alpha1).getImmutable();
        Element alpha2Plus = H.mul(rpk).add(alpha2).getImmutable();
        List<Element> proof3 = new ArrayList<>();
        //g,h,A,B,alpha1Plus,alpha2Plus
        proof3.add(g); //0
        proof3.add(h); //1
        proof3.add(compk); //2
        proof3.add(B); //3
        proof3.add(alpha1Plus); //4
        proof3.add(alpha2Plus); //5

        return proof3;
    }

    public static boolean Verify3(List<Element> proof3) {
        Element g = proof3.get(0).getImmutable();
        Element h = proof3.get(1).getImmutable();
        Element A = proof3.get(2).getImmutable();
        Element B = proof3.get(3).getImmutable();
        Element alpha1Plus = proof3.get(4).getImmutable();
        Element alpha2Plus = proof3.get(5).getImmutable();
        StringBuilder sb = new StringBuilder();
        sb.append(g);
        sb.append(h);
        sb.append(A);
        sb.append(B);
        byte[] Hbytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        Element H = Zr.newElementFromHash(Hbytes, 0, Hbytes.length);
        Element left = A.powZn(H).mul(B).getImmutable();
        Element right = g.powZn(alpha1Plus).mul(h.powZn(alpha2Plus)).getImmutable();

        return left.equals(right);
    }

    public static List<Element> Proof4(Element C1, Element u, Element k) {
        Element alpha1 = Zr.newRandomElement().getImmutable();
        Element B1 = u.powZn(alpha1).getImmutable();
//        Element alpha2 = Zr.newRandomElement().getImmutable();
//        Element B2 = h.powZn(alpha2).getImmutable();
        StringBuilder sb = new StringBuilder();
        sb.append(u);  //0
        sb.append(C1); //1
        sb.append(B1); //2
//        sb.append(h);  //3
//        sb.append(C2);
//        sb.append(B2);
        byte[] Hbytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        byte[] bytes = Hash.calculateSM3Hash(Hbytes);
        Element H = Zr.newElementFromHash(bytes, 0, bytes.length).getImmutable();
        Element alphaPlus1 = Zr.newZeroElement().sub(k).mul(H).add(alpha1).getImmutable();
//        Element alphaPlus2 = ks.mul(H).add(alpha2).getImmutable();
        List<Element> proof4 = new ArrayList<>();
        //C1部分：(u,C1,B1,alphaPlus1),C2部分:(h,C2,B2,alphaPlus2)
        proof4.add(u);  //0
        proof4.add(C1); //1
        proof4.add(B1); //2
        proof4.add(alphaPlus1); //3
//        proof4.add(h); //4
//        proof4.add(C2);
//        proof4.add(B2);
//        proof4.add(alphaPlus2);

        return proof4;
    }

    public static boolean Verify4(List<Element>proof4){
        Element u=proof4.get(0).getImmutable();
        Element C1=proof4.get(1).getImmutable();
        Element B1=proof4.get(2).getImmutable();
        Element alphaPlus1=proof4.get(3).getImmutable();
//        Element h=proof4.get(4).getImmutable();
//        Element C2=proof4.get(5).getImmutable();
//        Element B2=proof4.get(6).getImmutable();
//        Element alphaPlus2=((proof4.get(7)));
        StringBuilder sb = new StringBuilder();
        sb.append(u);
        sb.append(C1);
        sb.append(B1);
        byte[] Hbytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        byte[] bytes = Hash.calculateSM3Hash(Hbytes);
        Element H = Zr.newElementFromHash(bytes, 0, bytes.length).getImmutable();
        Element left1=C1.powZn(H).mul(B1).getImmutable();
        Element right1=u.powZn(alphaPlus1).getImmutable();

//        Element left2=C2.powZn(H).mul(B2).getImmutable();
//        Element right2=h.powZn(alphaPlus2).getImmutable();
        return left1.equals(right1);
//        return left1.equals(right1) && left2.equals(right2);
    }

    public static List<Element> Proof5(Element ind, Element keywordElement, Element g, Element k, Element g1, Element gn) {
        Element A = pairing.pairing(g,keywordElement).div(pairing.pairing(g1, gn)).getImmutable();
        Element alpha = Zr.newRandomElement().getImmutable();
        Element B = A.powZn(alpha).getImmutable();
        StringBuilder sb = new StringBuilder();
        sb.append(ind);
        sb.append(A);
        sb.append(B);
        byte[] HBytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        byte[] Hhash=Hash.calculateSM3Hash(HBytes);
        Element H = Zr.newElementFromHash(Hhash, 0, Hhash.length).getImmutable();
        Element alphaPlus = H.mul(k).add(alpha).getImmutable();
        List<Element> proof5 = new ArrayList<>();
        //ind,A,B,alphaPlus
        proof5.add(ind);
        proof5.add(A);
        proof5.add(B);
        proof5.add(alphaPlus);

        return proof5;
    }

    public static boolean Verify5(List<Element>proof5){
        Element ind=proof5.get(0).getImmutable();
        Element A=proof5.get(1).getImmutable();
        Element B=proof5.get(2).getImmutable();
        Element alphaPlus=proof5.get(3).getImmutable();
        StringBuilder sb = new StringBuilder();
        sb.append(ind);
        sb.append(A);
        sb.append(B);
        byte[] HBytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        byte[] Hhash=Hash.calculateSM3Hash(HBytes);
        Element H = Zr.newElementFromHash(Hhash, 0, Hhash.length).getImmutable();
        Element left=ind.powZn(H).mul(B).getImmutable();
        Element right=A.powZn(alphaPlus).getImmutable();

        return left.equals(right);
    }












}
