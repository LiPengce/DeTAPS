package com.lpc.pojo;

import java.util.Date;

public class BlockInfo {
    private int blockNumber;
    private String blockHash;
    private Date timestamp;
    private int transactionSize;
    private String gasLimit;
    private String totalDifficulty;
    private String parentHash;
    private String stateRoot;
    private String extraData;
    private String miner;

    // 构造函数
    public BlockInfo(int blockNumber, String blockHash, Date timestamp,int transactionSize, String gasLimit, String totalDifficulty, String parentHash, String stateRoot, String extraData,String miner) {
        this.blockNumber = blockNumber;
        this.blockHash = blockHash;
        this.timestamp = timestamp;
        this.transactionSize = transactionSize;
        this.gasLimit = gasLimit;
        this.totalDifficulty = totalDifficulty;
        this.parentHash = parentHash;
        this.stateRoot = stateRoot;
        this.extraData = extraData;
        this.miner = miner;
    }

    public String getMiner() {
        return miner;
    }

    public void setMiner(String miner) {
        this.miner = miner;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getTransactionSize() {
        return transactionSize;
    }

    @Override
    public String toString() {
        return "BlockInfo{" +
                "blockNumber=" + blockNumber +
                ", blockHash='" + blockHash + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", transactionSize=" + transactionSize +
                ", gasLimit='" + gasLimit + '\'' +
                ", totalDifficulty='" + totalDifficulty + '\'' +
                ", parentHash='" + parentHash + '\'' +
                ", stateRoot='" + stateRoot + '\'' +
                ", extraData='" + extraData + '\'' +
                '}';
    }

    public void setTransactionSize(int transactionSize) {
        this.transactionSize = transactionSize;
    }

    public String getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(String gasLimit) {
        this.gasLimit = gasLimit;
    }

    public String getTotalDifficulty() {
        return totalDifficulty;
    }

    public void setTotalDifficulty(String totalDifficulty) {
        this.totalDifficulty = totalDifficulty;
    }

    public String getParentHash() {
        return parentHash;
    }

    public void setParentHash(String parentHash) {
        this.parentHash = parentHash;
    }

    public String getStateRoot() {
        return stateRoot;
    }

    public void setStateRoot(String stateRoot) {
        this.stateRoot = stateRoot;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    // Getter 和 Setter 方法
    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}

