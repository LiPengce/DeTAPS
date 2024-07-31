package com.lpc.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class User {
    private Integer id;//主键id
    private String username;//用户名
    @JsonIgnore //转成json字符串时忽略
    private String password;//密码
}
