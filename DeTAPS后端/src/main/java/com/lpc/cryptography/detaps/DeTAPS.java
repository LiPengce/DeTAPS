package com.lpc.cryptography.detaps;

import com.lpc.cryptography.common.PairingProvider;
import com.lpc.cryptography.base.SIG;
import com.lpc.cryptography.util.PropertiesUtil;
import com.lpc.cryptography.util.Serialize;

import com.lpc.util.CustomSerializer;
import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.interfaces.ECPublicKey;
import java.util.*;


public class DeTAPS implements PairingProvider{
    private static final Element g=PairingProvider.g;
    private static final Element h=PairingProvider.h;

    public DeTAPS() {}

    public boolean Verify(String m, String finalsign) throws Exception {
        System.out.println("Verify start!");

        Properties PKprop = PropertiesUtil.loadProperties("PK.properties");
        String PKstr = PKprop.getProperty("PK");
        Map<String, Object> PK = (Map<String, Object>) Serialize.deserialize(Hex.decode(PKstr));

        List<Object> DeTAPSsign = (List<Object>) CustomSerializer.deserialize(Base64.getDecoder().decode(finalsign));
        List<Object> DTPKEcipherText = (List<Object>) DeTAPSsign.get(0);
        Element c1 = ((Element) DeTAPSsign.get(1)).getImmutable();
        Element c2 = ((Element) DeTAPSsign.get(2)).getImmutable();
        List<Element> inds = (List<Element>) DeTAPSsign.get(3);
        List<Object> pi = (List<Object>) DeTAPSsign.get(4);
        Map<String, Object> pkPlus = (Map<String, Object>) PK.get("pkPlus");
        List<Element> pk = (List<Element>) pkPlus.get("pk");
        byte[] eta = Base64.getDecoder().decode((String) DeTAPSsign.get(5));
        //验证ECDSA签名
        StringBuilder sb = new StringBuilder();
        sb.append(m).append(DTPKEcipherText).append(c1).append(c2).append(inds).append(pi);
        byte[] ECDbytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        ECPublicKey[] pks_set = (ECPublicKey[]) PK.get("pks_set");
        ECPublicKey pks = pks_set[0];
        boolean verifyresult = SIG.verifySignature(ECDbytes, eta, pks);

        if (verifyresult) {
            System.out.println("SIG scheme verify succeed !");
        } else {
            System.out.println("SIG scheme verify succeed !");
        }
        verifyresult = true;
        List<Element> proof1_2_1 = (List<Element>) pi.get(0);
        List<Element> proof1_2_2 = (List<Element>) pi.get(1);
        List<Element> proof1_3 = (List<Element>) pi.get(2);
        List<Element> proof2_1 = (List<Element>) pi.get(3);
        List<Element> proof2_2_1 = (List<Element>) pi.get(4);
        List<Element> proof2_2_2 = (List<Element>) pi.get(5);
        List<Element> proof2_3 = (List<Element>) pi.get(6);
        List<Element> proof3 = (List<Element>) pi.get(7);
        List<Element> proof4 = (List<Element>) pi.get(8);
        List<List<Element>> proof5 = (List<List<Element>>) pi.get(9);
        if (ZeroKnowledgeProof.Verify1_2_1(proof1_2_1)) {
            System.out.println("ZeroKnowledgeProof 1_1 verify succeed!");
        } else {
            System.out.println("ZeroKnowledgeProof 1_1 verify fail!");
            verifyresult = false;
        }
        if (ZeroKnowledgeProof.Verify1_2_1(proof1_2_1)) {
            System.out.println("ZeroKnowledgeProof 1_2_1 verify succeed!");
        } else {
            System.out.println("ZeroKnowledgeProof 1_2_1 verify fail!");
            verifyresult = false;
        }
        if (ZeroKnowledgeProof.Verify1_2_2(proof1_2_2)) {
            System.out.println("ZeroKnowledgeProof 1_2_2 verify succeed!");
        } else {
            System.out.println("ZeroKnowledgeProof 1_2_2 verify fail!");
            verifyresult = false;
        }
        if (ZeroKnowledgeProof.Verify1_3(g, h, proof1_3)) {
            System.out.println("ZeroKnowledgeProof 1_3 verify succeed!");
        } else {
            System.out.println("ZeroKnowledgeProof 1_3 verify fail!");
            verifyresult = false;
        }
        if (ZeroKnowledgeProof.Verify2_1(proof2_1, pk)) {
            System.out.println("ZeroKnowledgeProof 2_1 verify succeed!");
        } else {
            System.out.println("ZeroKnowledgeProof 2_1 verify fail!");
            verifyresult = false;
        }
        if (ZeroKnowledgeProof.Verify2_2_1(proof2_2_1)) {
            System.out.println("ZeroKnowledgeProof 2_2_1 verify succeed!");
        } else {
            System.out.println("ZeroKnowledgeProof 2_2_1 verify fail!");
            verifyresult = false;
        }
        if (ZeroKnowledgeProof.Verify2_2_2(proof2_2_2)) {
            System.out.println("ZeroKnowledgeProof 2_2_2 verify succeed!");
        } else {
            System.out.println("ZeroKnowledgeProof 2_2_2 verify fail!");
            verifyresult = false;
        }
        if (ZeroKnowledgeProof.Verify2_3(g, h, proof2_3)) {
            System.out.println("ZeroKnowledgeProof 2_3 verify succeed!");
        } else {
            System.out.println("ZeroKnowledgeProof 2_3 verify fail!");
            verifyresult = false;
        }
        if (ZeroKnowledgeProof.Verify3(proof3)) {
            System.out.println("ZeroKnowledgeProof 3 verify succeed!");
        } else {
            System.out.println("ZeroKnowledgeProof 3 verify fail!");
            verifyresult = false;
        }
        if (ZeroKnowledgeProof.Verify4(proof4)) {
            System.out.println("ZeroKnowledgeProof 4 verify succeed!");
        } else {
            System.out.println("ZeroKnowledgeProof 4 verify fail!");
            verifyresult = false;
        }
        for(List<Element>proof5_part:proof5){
            if (!ZeroKnowledgeProof.Verify5(proof5_part)) {
                System.out.println("ZeroKnowledgeProof 5 verify fail!");
                verifyresult = false;
            }
        }
        System.out.println("ZeroKnowledgeProof 5 verify succeed!");
        System.out.println("Verify end !");
        return verifyresult;
    }

}
