package com.lpc.util;

import com.lpc.blockchain.CustomGasProvider;
import com.lpc.blockchain.DeTAPSContract;
import com.lpc.common.PairingProvider;
import org.bouncycastle.util.encoders.Base64;
import org.jetbrains.annotations.TestOnly;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class Test implements PairingProvider {
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        //区块链
        String rpcUrl = "http://47.96.177.120:8545";
        String contractAddress = "0xa272588ca8c25e0ce90bd58ed64d8fb07643a229";
        final int chainID = 12345;

        ClassLoader classLoader = Test.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("UTC--2024-05-12T08-00-03.031907578Z--cb92549999f4576f105fc31ac01e01cc377a336d");
        if (inputStream == null) {
            throw new RuntimeException("Resource file not found");
        }

        // 将输入流写入临时文件
        File tempFile = File.createTempFile("keystore", ".tmp");
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            String password_ = "123";
            Web3j web3j = Web3j.build(new HttpService(rpcUrl));
            Credentials credentials = WalletUtils.loadCredentials(password_, tempFile.getAbsolutePath());
            RawTransactionManager rawTransactionManager = new RawTransactionManager(web3j, credentials, chainID);

//            public static final BigInteger GAS_LIMIT = BigInteger.valueOf(9000000L);
//            public static final BigInteger GAS_PRICE = BigInteger.valueOf(4100000000L);
            BigInteger gasPrice = BigInteger.valueOf(5000000000L);
            BigInteger gasLimit = BigInteger.valueOf(18000000L); // 设置合适的Gas限制

            CustomGasProvider customGasProvider = new CustomGasProvider(gasPrice, gasLimit);
            DeTAPSContract contract = DeTAPSContract.load(contractAddress, web3j, rawTransactionManager, customGasProvider);
            System.out.println("load complete");

            String m = "123456";
            byte[] bytes = new byte[16338];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = 1;
            }
            String finalSign = Base64.toBase64String(bytes);
            System.out.println("finalSign.length() = " + finalSign.length());
            TransactionReceipt txReceipt = contract.submitM_DeTAPSsign(m, finalSign).send();
            System.out.println(txReceipt.getBlockHash());
            System.out.println(txReceipt.getTransactionHash());
            System.out.println(txReceipt.getBlockNumber());
            System.out.println("success ");
        } catch (CipherException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}