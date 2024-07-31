package com.lpc.detaps;

import com.lpc.common.PairingProvider;

public class Setup implements PairingProvider {
    public static void main(String[] args) throws Exception {
        final int n=10;
        final int n1=5;
        final int n2=5;
        final int n3=8;
        int t=4;
        final int tpie=4;
        DeTAPS deTAPS=new DeTAPS(n,t,n1,n2,n3,tpie);
//        //n是最大签名者的数量，n1是Combiner的数量，n2是Tracer的数量，n3是Notaries的数量
        deTAPS.Setup(512, n, n1, n2, n3, t,tpie);
//        System.out.println("Setup结束！");
    }
}
