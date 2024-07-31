package com.lpc.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Notary extends User{
    @JsonIgnore
    private String upk; //公钥
}
