package com.lpc.util;

import com.lpc.cryptography.common.PairingProvider;
import it.unisa.dia.gas.jpbc.Element;

/**
 * @author lpc
 * @version 1.0
 * @since 2024-05-16
 */
public class Test implements PairingProvider{
    public static void main(String[] args) {
        Element g = G.newRandomElement().getImmutable();
        Element r1 = Zr.newRandomElement().getImmutable();
        Element r2 = Zr.newRandomElement().getImmutable();

        Element g1=g.powZn(r1).mul(g.powZn(r2)).getImmutable();
        Element g2=g.powZn(r1.add(r2)).getImmutable();
        System.out.println("g1.equals(g2) = " + g1.equals(g2));
    }
}
