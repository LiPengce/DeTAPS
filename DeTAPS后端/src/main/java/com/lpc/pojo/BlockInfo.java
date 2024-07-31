package com.lpc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}

