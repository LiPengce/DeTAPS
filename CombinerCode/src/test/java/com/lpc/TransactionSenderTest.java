package com.lpc;

import com.lpc.blockchain.DeTAPSContract;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;

import static org.junit.Assert.assertTrue;

/**
 * @author lpc
 * @version 1.0
 * @since 2024-05-18
 */

public class TransactionSenderTest {

    @Test
    public void testSomething() {
        assertTrue(true);
    }

    @Test
    public void testSendTransaction() throws Exception {
        String rpcUrl = "http://47.96.177.120:8545";
        String contractAddress = "0xe9a22b3938dacc2ba4a4e36154b578ff93a5a0c4";
        int chainID = 12345;
        String keystoreFilePath = "D:\\Geth\\AliyunTest\\UTC--2024-05-16T06-45-28.964650523Z--131418905327d9fa3937a36ef7d3b023de91b9cc";
        String password = "123";

        Web3j web3j = Web3j.build(new HttpService(rpcUrl));
        System.out.println(web3j.web3ClientVersion().send().getWeb3ClientVersion());

        Credentials credentials = WalletUtils.loadCredentials(password, keystoreFilePath);
        RawTransactionManager rawTransactionManager = new RawTransactionManager(web3j, credentials, chainID);
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        DeTAPSContract contract = DeTAPSContract.load(contractAddress, web3j, rawTransactionManager, contractGasProvider);

        String encshare = "BFyKDR6RWc1eUmxRmdVpF1lfsiCX8cXKYa0TxHTBV+NqDPk0C...";

        // 上传到区块链
        TransactionReceipt txReceipt = contract.submitSignShare(new BigInteger("2"), "117cf410532f92a47dee245ce9b11ff71f578ebd763eb3bbea44ebd043d018f3", encshare).send();
        System.out.println(txReceipt.getBlockHash());
        System.out.println(txReceipt.getTransactionHash());
        System.out.println(txReceipt.getBlockNumber());
        System.out.println("Success");
    }
}
