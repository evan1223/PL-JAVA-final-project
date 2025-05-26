package com.demo.util;

import org.springframework.stereotype.Component;

@Component
public class UserSession {
    private String username;
    private boolean loggedIn = false;

    public void login(String username) {
        this.username = username;
        this.loggedIn = true;
    }

    public void logout() {
        this.username = null;
        this.loggedIn = false;
    }

    public String getUsername() {
        return username;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}