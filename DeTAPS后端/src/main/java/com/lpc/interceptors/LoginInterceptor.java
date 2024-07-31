package com.lpc.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.lpc.util.JwtUtil;
import com.lpc.util.ThreadLocalUtil;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

//拦截器
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // 检查是否是OPTIONS请求
        if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            // 是预检请求，直接返回true
            return true;
        }

        //token verify
        String token = request.getHeader("Authorization");

        System.out.println("token = " + token);
        System.out.println();
        try {
            Map<String, Object> claims = JwtUtil.tokenVerify(token);
            ThreadLocalUtil.set(claims);

            return true;
        }catch (Exception e){
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ThreadLocalUtil.remove();
        System.out.println("END!");
    }
}
