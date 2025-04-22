package com.demo.service;

import org.springframework.stereotype.Component;

@Component
public class AuthService {
    public boolean authenticate(String username, String password) {
        return username.equals("admin") && password.equals("password");
    }
}
