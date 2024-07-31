package com.lpc.pojo;

/**
 * @author lpc
 * @version 1.0
 * @since 2024-04-21
 */
public class TransactionInfo {
    String From;
    String To;
    int BlockNumber;
    boolean Status;
    String Hash;
    int Nonce;
    int GasPrice;
    int GasLimit;
    String InputData;



    public String getFrom() {
        return From;
    }

    @Override
    public String toString() {
        return "TransactionInfo{" +
                "From='" + From + '\'' +
                ", To='" + To + '\'' +
                ", BlockNumber=" + BlockNumber +
                ", Status=" + Status +
                ", Hash='" + Hash + '\'' +
                ", Nonce=" + Nonce +
                ", GasPrice=" + GasPrice +
                ", GasLimit=" + GasLimit +
                ", InputData='" + InputData + '\'' +
                '}';
    }

    public TransactionInfo(String from, String to, int blockNumber, boolean status, String hash, int nonce, int gasprice, int gaslimit, String inputdata) {
        From = from;
        To = to;
        this.BlockNumber = blockNumber;
        Status = status;
        Hash = hash;
        Nonce = nonce;
        GasPrice = gasprice;
        GasLimit = gaslimit;
        InputData = inputdata;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public String getHash() {
        return Hash;
    }

    public void setHash(String hash) {
        Hash = hash;
    }

    public int getNonce() {
        return Nonce;
    }

    public void setNonce(int nonce) {
        Nonce = nonce;
    }

    public int getGasPrice() {
        return GasPrice;
    }

    public void setGasPrice(int gasPrice) {
        GasPrice = gasPrice;
    }

    public int getGasLimit() {
        return GasLimit;
    }

    public void setGasLimit(int gasLimit) {
        GasLimit = gasLimit;
    }

    public int getBlockNumber() {
        return BlockNumber;
    }

    public void setBlockNumber(int blockNumber) {
        BlockNumber = blockNumber;
    }

    public String getInputData() {
        return InputData;
    }

    public void setInputData(String inputData) {
        InputData = inputData;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }
}
