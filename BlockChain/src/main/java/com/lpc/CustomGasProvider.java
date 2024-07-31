package com.lpc;

import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;

/**
 * @author lpc
 * @version 1.0
 * @since 2024-04-30
 */
public class CustomGasProvider implements ContractGasProvider {
    private BigInteger gasPrice;
    private BigInteger gasLimit;

    public CustomGasProvider(BigInteger gasPrice, BigInteger gasLimit) {
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
    }

    @Override
    public BigInteger getGasPrice(String s) {
        return gasPrice;
    }

    @Override
    public BigInteger getGasPrice() {
        return gasPrice;
    }

    @Override
    public BigInteger getGasLimit(String s) {
        return gasLimit;
    }

    @Override
    public BigInteger getGasLimit() {
        return gasLimit;
    }
}
