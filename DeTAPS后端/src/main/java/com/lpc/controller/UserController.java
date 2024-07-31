package com.lpc.controller;

import com.lpc.pojo.Result;
import com.lpc.pojo.User;
import com.lpc.service.UserService;
import com.lpc.util.JwtUtil;
import com.lpc.cryptography.base.Hash;

import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Validated
@CrossOrigin //支持跨域
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result register(String role,String username,String password){
        if(username!=null && username.length()>=5 && username.length()<=16
                && password!=null && password.length()>=5 && password.length()<=16) {
            //查询用户
            User u = userService.findByUserName(role,username);
            //注册
            if (u == null) {
                userService.register(role,username, password);
                return Result.success();
            } else {
                //占用
                System.out.println("被占用！");
                return Result.error("用户名已被占用!");
            }
        }else {
            return Result.error("参数不合法");
        }
    }

    @PostMapping("/login")
    public Result<String>login(@Pattern(regexp = "^(签名者|公证人)$") String role,@Pattern(regexp = "^\\S{5,16}$")String username, @Pattern(regexp = "^\\S{5,16}$")String password){
        User u = userService.findByUserName(username,role);
        if(u==null){
            return Result.error("用户名错误");
        }
        if (u.getPassword().equals(Hash.calculateSM3Hash(password))) {
            Map<String,Object> claims=new HashMap<>();
            claims.put("id",u.getId());
            claims.put("username",u.getUsername());
            String token= JwtUtil.tokenGen(claims);

            System.out.println("token = " + token);

            return Result.success(token);
        }
        return Result.error("密码错误");
    }
}
