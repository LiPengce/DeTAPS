package com.lpc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author lpc
 * @version 1.0
 * @since 2024-04-21
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
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
    Date createdAt;
}
