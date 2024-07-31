package com.lpc.cryptography.detaps;

import com.lpc.cryptography.common.PairingProvider;
import com.lpc.cryptography.util.PropertiesUtil;
import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.util.encoders.Hex;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class DeTest implements PairingProvider {
    public static void main(String[] args) throws Exception {
//        final int n=30;
//        final int n1=5;
//        final int n2=5;
//        final int n3=6;
//        final int t=5;
//        final int tpie=3;
//        DeTAPS deTAPS=new DeTAPS(n,t,n1,n2,n3,tpie);
//        //n是最大签名者的数量，n1是Combiner的数量，n2是Tracer的数量，n3是Notaries的数量
////        deTAPS.Setup(512, n, n1, n2, n3, t,tpie);
//        System.out.println("Setup结束！");
//        Element uvk1=Zr.newElementFromBytes(Hex.decode("5285c84c99d4e1194e926b75ae9bab4551559d5e")).getImmutable();
//        Element uvk2=Zr.newElementFromBytes(Hex.decode("5f57b6a2eac6b6e9e899a68888a4bca511f1bf16")).getImmutable();
//        Element uvk3=Zr.newElementFromBytes(Hex.decode("03a5dc02ff6689871bea5013464982760e80b8a3")).getImmutable();
        //签名部分
//        String m="wmclpc1341520";

//        Element[]Specupk=new Element[3];
//        Specupk[0]=uvk1;
//        Specupk[1]=uvk2;
//        Specupk[2]=uvk3;

//        int []C={1,3,5,7,9}; //C就是签名者的下标
//
//        String gid="sadasdasdasdasdklsajdoasljdkasd";
//
//        String sk1="78bd96e1dc1cf62b4805b974dae3dc3bddb90ab1";
//        String sk3="68b1ecb41974839fb28b3f38a02cc1a2c05ea56f";
//        String sk5="48c778641944b6ed272af540c74f91bbe61075c5";
//        String sk7="664316a9358ee155f82b5383a7e87b21777b306b";
//        String sk9="45a1022ad0d312e79417b7b494e1d28d69a92c4f";

//        FutureTask<String>signer1=new FutureTask<>(new SignCall(deTAPS,sk1, m,Specupk, gid));
//        FutureTask<String>signer2=new FutureTask<>(new SignCall(deTAPS,sk3, m,Specupk, gid));
//        FutureTask<String>signer3=new FutureTask<>(new SignCall(deTAPS,sk5, m,Specupk, gid));
//        FutureTask<String>signer4=new FutureTask<>(new SignCall(deTAPS,sk7, m,Specupk, gid));
//        FutureTask<String>signer5=new FutureTask<>(new SignCall(deTAPS,sk9, m,Specupk, gid));

//        Thread t1=new Thread(signer1);
//        Thread t2=new Thread(signer2);
//        Thread t3=new Thread(signer3);
//        Thread t4=new Thread(signer4);
//        Thread t5=new Thread(signer5);

//        t1.start();
//        t2.start();
//        t3.start();
//        t4.start();
//        t5.start();

//        List<String>encryptedshares=Collections.synchronizedList(new ArrayList<>());
//        try {
//            String result = signer1.get();
//            encryptedshares.add(result);
//            // 处理成功的情况
//        } catch (InterruptedException | ExecutionException e) {
//            Throwable cause = e.getCause(); // 获取原始异常
//            cause.printStackTrace(); // 打印异常信息
//            // 处理异常的情况
//        }
//        try {
//            String result = signer2.get();
//            encryptedshares.add(result);
//            // 处理成功的情况
//        } catch (InterruptedException | ExecutionException e) {
//            Throwable cause = e.getCause(); // 获取原始异常
//            cause.printStackTrace(); // 打印异常信息
//            // 处理异常的情况
//        }
//        try {
//            String result = signer3.get();
//            encryptedshares.add(result);
//            // 处理成功的情况
//        } catch (InterruptedException | ExecutionException e) {
//            Throwable cause = e.getCause(); // 获取原始异常
//            cause.printStackTrace(); // 打印异常信息
//            // 处理异常的情况
//        }
//        try {
//            String result = signer4.get();
//            encryptedshares.add(result);
//            // 处理成功的情况
//        } catch (InterruptedException | ExecutionException e) {
//            Throwable cause = e.getCause(); // 获取原始异常
//            cause.printStackTrace(); // 打印异常信息
//            // 处理异常的情况
//        }
//        try {
//            String result = signer5.get();
//            encryptedshares.add(result);
//            // 处理成功的情况
//        } catch (InterruptedException | ExecutionException e) {
//            Throwable cause = e.getCause(); // 获取原始异常
//            cause.printStackTrace(); // 打印异常信息
//            // 处理异常的情况
//        }
//
//        System.out.println("Test Sign end!");
//        long l1 = System.currentTimeMillis();
//        String DeTAPSsign = deTAPS.Combine(encryptedshares,C, gid);
//        long l2 = System.currentTimeMillis();
//        System.out.println(String.format("Combine takes time: %f s",(l2-l1)/1000.0));
//        System.out.println("DeTAPS Sign end");
//
//        long l3 = System.currentTimeMillis();
//        boolean verifyresult = deTAPS.Verify(m,DeTAPSsign);
//        long l4 = System.currentTimeMillis();
//        System.out.println(String.format("Verify takes time %f s",(l4-l3)/1000.0));
//        System.out.println("DeTAPS Signature result: "+verifyresult);
//
//        Properties kaggProp = PropertiesUtil.loadProperties("kagg.properties");
//        String kaggStr = kaggProp.getProperty("kagg");
//        Element kagg=G.newElementFromBytes(Hex.decode(kaggStr)).getImmutable();
//
//        Element usk1=G.newElementFromBytes(Hex.decode("4818144046eb8c1937ec0241ae560d64f78c50d38a8d4612c35f37f0c058ef821e6b12341ff627507af3595adde295994bda26f12c2d89ec9ac3fc478abd9d989d1e5e76022c0d257b027ef36569c6fd07f135bff8b481c10923620e3b1d068b9017dfcb5f97df8682fc38ba24e92c9996003e88b4fef6d156e423bd3242b81e")).getImmutable();
//        Element usk2=G.newElementFromBytes(Hex.decode("18c5cdbf0f2204c29f8517c64bbfc21ba6fc5ebab37366c75f369764f01180727ff02a52114ec252e7e9d29eda7be97eeb12419d8950d67e69567bff51ea3e5c4961ac5a7165e341a37fc67e1fb076b60707aee233b9bead67405b0432d40a556935384fec467969a7d3cc55b318e88e3f4d3fd1cf4f4277f706cd88e7076b95")).getImmutable();
//        Element usk3=G.newElementFromBytes(Hex.decode("17ef4428eae9e85d9559879122614d859bdd0e9ecf913665cca616d4a29c407ecb436c738aeb0ed1df4b9d4b7055c42207013d0d313848991d40498ff1cc3b4220c7b4ce6df7506f9b23466e6b73e9b483c31384819ee9ec9f045cb1e85a8ee90a159ac40c840e510734574c1b45329423a6c7e6b6e46dc8b241886156cd85aa")).getImmutable();
//        long l5 = System.currentTimeMillis();
//        String share1 = deTAPS.Trace1(kagg,uvk1, usk1);
//        long l6 = System.currentTimeMillis();
//        System.out.println(String.format("Trace1 takes time %f s",(l6-l5)/1000.0));
//        String share2 = deTAPS.Trace1(kagg,uvk2, usk2);
//        String share3 = deTAPS.Trace1(kagg,uvk3, usk3);
//
//        List<String> dtpkeShares =new ArrayList<>();
//        dtpkeShares.add(share1);
//        dtpkeShares.add(share2);
//        dtpkeShares.add(share3);
//
//        Element[]T=new Element[tpie];
//        T[0]=uvk1;
//        T[1]=uvk2;
//        T[2]=uvk3;
//        long l7 = System.currentTimeMillis();
//        int[] SignerIndexs = deTAPS.Trace2(dtpkeShares,m, DeTAPSsign, T);
//        long l8 = System.currentTimeMillis();
//        System.out.println(String.format("Trace2 takes time %f s",(l8-l7)/1000.0));
//        System.out.println("trace result： "+Arrays.toString(SignerIndexs));

    }
}
