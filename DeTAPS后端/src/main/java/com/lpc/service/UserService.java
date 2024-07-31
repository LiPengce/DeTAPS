package com.lpc.service;

import com.lpc.pojo.User;

public interface UserService {

    User findByUserName(String role, String username);

    void register(String role,String username, String password);
}
