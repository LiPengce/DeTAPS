package com.lpc;

import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.util.Random;


public class TransactionSender {

    private String rpcUrl;
    private String contractAddress;
    private int chainID;
    private String keystoreFilePath;
    private String password;

    public TransactionSender(String rpcUrl, String contractAddress, int chainID, String keystoreFilePath, String password) {
        this.rpcUrl = rpcUrl;
        this.contractAddress = contractAddress;
        this.chainID = chainID;
        this.keystoreFilePath = keystoreFilePath;
        this.password = password;
    }

    public void sendTransaction() throws Exception {
        Web3j web3j = Web3j.build(new HttpService(rpcUrl));
        System.out.println(web3j.web3ClientVersion().send().getWeb3ClientVersion());

        Credentials credentials = WalletUtils.loadCredentials(password, keystoreFilePath);
        RawTransactionManager rawTransactionManager = new RawTransactionManager(web3j, credentials, chainID);
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        DeTAPSContract contract = DeTAPSContract.load(contractAddress, web3j, rawTransactionManager, contractGasProvider);

        String encshare = "BFyKDR6RSZrMzZh1UgEZrzuCeps5w1l6y410RB1Fv4MY1Tp8GUI3O22FIzOBQ2TUQkS";
        Random random = new Random();
        double v = random.nextDouble();
        int i = random.nextInt();
        // 上传到区块链
        TransactionReceipt txReceipt = contract.submitSignShare(new BigInteger("2"), Hash.sha3(String.valueOf(v)+String.valueOf(i)), encshare).send();
        System.out.println(txReceipt.getTransactionHash());
        System.out.println(txReceipt.getBlockNumber());
        System.out.println("Success");

    }

}
