package com.lpc.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor //无参构造
@AllArgsConstructor //全参构造
@Data
public class SignResponse {
    private int CombinerIndex;
    private String gid;
    private String share;
}
