package com.lpc.cryptography.blockchain;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class Blockchain {
    private Web3j web3j;
    private Credentials credentials;
    private RawTransactionManager transactionManager;
    private ContractGasProvider gasProvider;
    private String contractAddress;
    private int chainID;

    public Blockchain(String rpcUrl, String contractAddress, int chainID, String keystoreFilePath, String password) throws Exception {
        this.web3j = Web3j.build(new HttpService(rpcUrl));
        this.credentials = WalletUtils.loadCredentials(password, keystoreFilePath);
        this.chainID = chainID;
        this.contractAddress = contractAddress;
        this.transactionManager = new RawTransactionManager(web3j, credentials, this.chainID);

        BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
        this.gasProvider = new DynamicGasProvider(gasPrice, BigInteger.valueOf(3000000L));
        try {
            // 检查是否连接成功，获取客户端版本
            Web3ClientVersion web3ClientVersion = this.web3j.web3ClientVersion().send();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            System.out.println("成功连接，版本号：: " + clientVersion);
        } catch (Exception e) {
            System.out.println("连接失败！ ");
            e.printStackTrace();
        }
    }

    //创建动态汽油费Provider
    public static class DynamicGasProvider implements ContractGasProvider {
        private BigInteger gasPrice;
        private BigInteger gasLimit;

        public DynamicGasProvider(BigInteger gasPrice, BigInteger gasLimit) {
            this.gasPrice = gasPrice;
            this.gasLimit = gasLimit;
        }

        public BigInteger getGasPrice(String contractFunc) {
            return gasPrice;
        }

        public BigInteger getGasPrice() {
            return gasPrice;
        }

        public BigInteger getGasLimit(String contractFunc) {
            return gasLimit;
        }

        public BigInteger getGasLimit() {
            return gasLimit;
        }
    }

    //得到一个合约实例
    public DeTAPSContract getContractInstance() {
        return new DeTAPSContract(contractAddress, web3j, transactionManager, gasProvider);
    }

    //每个签名者上传（m,share）
    public String sendMessageAndshare(int gid, String message, String encshare) throws Exception {
        DeTAPSContract contract = getContractInstance();
        TransactionReceipt tx = contract.sendMessageAndshare(new BigInteger(String.valueOf(gid)), message, encshare).send();
        String transactionHash = tx.getTransactionHash();
        if (tx != null && tx.isStatusOK()) {
            System.out.println("签名份额上传成功！ 交易哈希值为： " + transactionHash);
        } else {
            System.err.println("交易失败！ 交易哈希值为： " + transactionHash);
        }
        EthTransaction ethTransaction = web3j.ethGetTransactionByHash(transactionHash).send();
        Transaction transaction = ethTransaction.getResult();
        BigInteger blockNumber = transaction.getBlockNumber();
        EthGetTransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt(transactionHash).send();
        TransactionReceipt receipt = transactionReceipt.getResult();
        EthBlock ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(blockNumber), true).send();
        EthBlock.Block block = ethBlock.getBlock();

        System.out.println("区块信息： "+block);
        System.out.println("transactionHash = " + transactionHash);
        System.out.println("transactionHash.length() = " + transactionHash.length());
        String status = receipt.getStatus();
        System.out.println("status = " + (status.equals("0x1") ? "succeed" : "fail") );
        System.out.println("blockNumber = " + blockNumber);
        BigInteger gasPrice = transaction.getGasPrice();
        System.out.println("gasPrice = " + gasPrice);

        BigInteger timestamp = block.getTimestamp();
        // 将时间戳转换为Instant对象
        Instant instant = Instant.ofEpochSecond(timestamp.longValue());
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        System.out.println("Timestamp = " + dateTime);

        BigInteger gasLimit = block.getGasLimit();
        System.out.println("gasLimit = " + gasLimit);

        BigInteger gasUsed = receipt.getGasUsed();
        System.out.println("gasUsed = " + gasUsed);

        String from = transaction.getFrom();
        System.out.println("from = " + from);
        System.out.println("from.length() = " + from.length());

        String to = transaction.getTo();
        System.out.println("to = " + to);

        String inputData = transaction.getInput();
        System.out.println("inputData = " + inputData);

        return transactionHash;
    }

    public Map.Entry<String,List<String>>getAllATSshares(int gid) throws Exception {
        DeTAPSContract contract=getContractInstance();
        Tuple2<String, List<String>> result = contract.getAllATSshares(new BigInteger(String.valueOf(gid))).send();
        return new AbstractMap.SimpleEntry<>(result.component1(),result.component2());
    }

    public void sendMessageAndDeTAPSsign(String message, String DeTAPSsign) throws Exception {
        DeTAPSContract contract=getContractInstance();
        TransactionReceipt tx = contract.sendMessageAndDeTAPSsign(message,DeTAPSsign).send();
        if (tx.isStatusOK()) {
            System.out.println("DeTPAS签名上传成功！ 交易哈希值为： " + tx.getTransactionHash());
        } else {
            System.err.println("交易失败！ 交易哈希值为： " + tx.getTransactionHash());
        }
    }

    public int getmessage_DeTAPSsigncount() throws Exception {
        DeTAPSContract contract=getContractInstance();
        BigInteger count = contract.getmessage_DeTAPSsigncount().send();

        return count.intValue();
    }

    public Map.Entry<String,String> getM_DeTAPSsign(int index) throws Exception {
        DeTAPSContract contract=getContractInstance();
        Tuple2<String, String> tuple2 = contract.getM_DeTAPSsign(new BigInteger(String.valueOf(index))).send();
        String message=tuple2.component1();
        String DeTAPSsign=tuple2.component2();

        return new AbstractMap.SimpleEntry<>(message,DeTAPSsign);
    }

    public void send_decShare(int gid, String decshare) throws Exception {
        DeTAPSContract contract=getContractInstance();
        TransactionReceipt tx = contract.send_decShare(new BigInteger(String.valueOf(gid)), decshare).send();
        if (tx.isStatusOK()) {
            System.out.println("公证人解密份额上传成功！ 交易哈希值为： " + tx.getTransactionHash());
        } else {
            System.err.println("交易失败！ 交易哈希值为： " + tx.getTransactionHash());
        }
    }

    public List<String> getNotariesShares(int gid) throws Exception {
        DeTAPSContract contract=getContractInstance();
        List<String>result = contract.getNotariesShares(new BigInteger(String.valueOf(gid))).send();

        return result;
    }

}
