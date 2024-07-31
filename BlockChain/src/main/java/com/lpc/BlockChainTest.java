package com.lpc;

import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author lpc
 * @version 1.0
 * @since 2024-05-18
 */
public class BlockChainTest {

    @Test
    public void testSendTransaction() throws Exception {
//        String rpcUrl = "http://47.96.177.120:8545";
//        String contractAddress = "0xe9a22b3938dacc2ba4a4e36154b578ff93a5a0c4";
//        int chainID = 12345;
//        String privateKey = "dfff9ae9aa6ae06489d39d2184c942a9689646bcbd33cbc0b8f50c673d8ba040";
//        BigInteger gasPrice = BigInteger.valueOf(5000000000L);
//        BigInteger gasLimit = BigInteger.valueOf(14000000L); // 设置合适的Gas限制
//
//        Web3j web3j = Web3j.build(new HttpService(rpcUrl));
//        System.out.println(web3j.web3ClientVersion().send().getWeb3ClientVersion());
//
//        Credentials credentials = Credentials.create(privateKey);
//        RawTransactionManager rawTransactionManager = new RawTransactionManager(web3j, credentials, chainID);
//        ContractGasProvider contractGasProvider = new CustomGasProvider(gasPrice, gasLimit);
//        DeTAPSContract contract = DeTAPSContract.load(contractAddress, web3j, rawTransactionManager, contractGasProvider);
//
//        String encshare = "BFyKDR6RWc1eUmxRmdVpF1lfs";
//
//        // 上传到区块链
//        TransactionReceipt txReceipt = contract.submitSignShare(new BigInteger("2"), "117cf410532f92a47dee245ce9b11ff71f578ebd763eb3bbea44ebd043d018f3", encshare).send();
//        System.out.println(txReceipt.getBlockHash());
//        System.out.println(txReceipt.getTransactionHash());
//        System.out.println(txReceipt.getBlockNumber());
//        System.out.println("Success");

        String rpcUrl = "http://47.96.177.120:8545";
        String contractAddress = "0xe9a22b3938dacc2ba4a4e36154b578ff93a5a0c4";
        int chainID = 12345;
        String privateKey = "YOUR_PRIVATE_KEY_HERE";  // Replace with your private key
        BigInteger gasLimit = BigInteger.valueOf(5000000);
        BigInteger gasPrice = Convert.toWei("40", Convert.Unit.GWEI).toBigInteger();

        Web3j web3j = Web3j.build(new HttpService(rpcUrl));
        System.out.println(web3j.web3ClientVersion().send().getWeb3ClientVersion());

        Credentials credentials = Credentials.create(privateKey);
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                credentials.getAddress(), DefaultBlockParameterName.LATEST).send();
        AtomicLong nonce = new AtomicLong(ethGetTransactionCount.getTransactionCount().longValue());

        ContractGasProvider contractGasProvider = new CustomGasProvider(gasPrice, gasLimit);
        DeTAPSContract contract = DeTAPSContract.load(contractAddress, web3j, credentials, contractGasProvider);

        String encshare = "BFyKDR6RWc1eUmxRmdVpF1lfs";

        // 手动管理 Nonce 并发送交易
        BigInteger currentNonce = BigInteger.valueOf(nonce.getAndIncrement());
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                currentNonce, gasPrice, gasLimit, contractAddress,
                contract.submitSignShare(new BigInteger("2"), "117cf410532f92a47dee245ce9b11ff71f578ebd763eb3bbea44ebd043d018f3", encshare).encodeFunctionCall());

        RawTransactionManager transactionManager = new RawTransactionManager(
                web3j, credentials, chainID);

        TransactionReceipt txReceipt = transactionManager.sendTransaction(
                gasPrice, gasLimit, contractAddress, rawTransaction.getData(), BigInteger.ZERO).getTransactionReceipt().get();

        // 打印交易结果
        System.out.println(txReceipt.getBlockHash());
        System.out.println(txReceipt.getTransactionHash());
        System.out.println(txReceipt.getBlockNumber());
        System.out.println("Success");

    }

}



