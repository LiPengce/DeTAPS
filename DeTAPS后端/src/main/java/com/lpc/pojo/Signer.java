package com.lpc.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Signer extends User{
    @JsonIgnore
    private String pk; //公钥
}
