package com.lpc.crypto;

import com.lpc.common.PairingProvider;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

import java.util.HashMap;
import java.util.Map;

public class COM implements PairingProvider {
    private static Pairing bp = PairingProvider.pairing;

    public static Map<String,Element>Setup(){
        Element g=PairingProvider.g.getImmutable();
        Element h=PairingProvider.h.getImmutable();
        Map<String,Element>publicTriple=new HashMap<>();
        publicTriple.put("g",g);
        publicTriple.put("h",h);

        return publicTriple;
    }

    public static Element Commit(Element x,Element r,Map<String,Element>publicTriple){
        Element g=publicTriple.get("g").getImmutable();
        Element h=publicTriple.get("h").getImmutable();
        Element com=g.powZn(x).mul(h.powZn(r)).getImmutable();
        return com;
    }

    public static boolean Verify(Element x,Element r,Element com,Map<String,Element>publicTriple){
        Element g= publicTriple.get("g").getImmutable();
        Element h= publicTriple.get("h").getImmutable();
        Element comPlus=g.powZn(x).mul(h.powZn(r)).getImmutable();
        return  comPlus.equals(com);
    }

}
