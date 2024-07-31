package com.lpc.cryptography.common;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.util.encoders.Hex;

import java.util.stream.IntStream;

public interface PairingProvider {
    Pairing pairing=PairingFactory.getPairing("a.properties");
    Field Zr=pairing.getZr();
    Field G=pairing.getG1();
    Field G2=pairing.getG2();
    Field GT=pairing.getGT();
    Element g=G.newElementFromBytes(Hex.decode("69496dcb1b91cb7b4897c1c26f741ec0318fd48d3ee28e12d7e4d2e1039c6435c2ee1ca797c565b79d7f6c4b2ab3286af1793c17b0d488a1634b27a01e6e6f3d58b3b902601c80e689c67940eac2fbe12028ece4c2d5292dc63b8386591eb90e19eb0b3c6b038787be0ffb66639124ac56504bb6ad3edcb9bf47e6cac09fa83b")).getImmutable();
    Element h=G.newElementFromBytes(Hex.decode("8c9ad1984408290564c3aa621037ec4ef1271efc996c032ca9283e025920160e5952044c4aa89513acac3452fb0d0a637bbf521eea3318a91a943764c6a869300a35a9dd967c4fddcae3de9d00c25d4142168cc64062afe509ed9781a52e8d33292cf27c369840eead24177b3e1fd44498e60cabedcd7ba2fdc5ffd3fe96d332")).getImmutable();
    int[] SIGNER_GROUPS=IntStream.rangeClosed(1,100).toArray();

}
