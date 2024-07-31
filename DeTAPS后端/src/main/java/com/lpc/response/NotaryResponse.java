package com.lpc.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NotaryResponse {
    private int TracerId;
    private int DeTAPSsignId;
    private String notaryShare;
}
