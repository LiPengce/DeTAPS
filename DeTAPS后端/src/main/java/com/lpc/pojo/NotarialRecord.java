package com.lpc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author lpc
 * @version 1.0
 * @since 2024-05-13
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NotarialRecord {
    private String companyName;
    private String crimeType;
    private String crimeDescription;
    private int signerID;
    private Date timestamp;
}
