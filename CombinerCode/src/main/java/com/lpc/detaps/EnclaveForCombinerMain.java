package com.lpc.detaps;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class EnclaveForCombinerMain {
    public static void main(String[] args) throws Exception {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
        DeTAPS deTAPS=new DeTAPS();
        deTAPS.Combine();
    }
}
