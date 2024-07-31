package com.lpc.service.impl;

import com.lpc.cryptography.base.Hash;
import com.lpc.mapper.UserMapper;
import com.lpc.pojo.Signer;
import com.lpc.pojo.User;
import com.lpc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUserName(String role, String username) {
        return userMapper.findUserByName(role,username);
    }

    @Transactional
    @Override
    public void register(String role, String username, String password) {
        String sm3psd= Hash.calculateSM3Hash(password);
        if("公证人".equals(role)){
            userMapper.addNotary(username,sm3psd);
        } else if ("签名者".equals(role)) {
            Integer currentId = userMapper.getMaxIdFromSigner();
            String pk = userMapper.getpkById(currentId);
            userMapper.addSigner(username,sm3psd,pk);
        }
    }




}
