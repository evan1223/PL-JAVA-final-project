package com.demo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;
import com.demo.service.AuthService;
import com.demo.util.SceneManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Component
public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML private Label errorLabel;
    @Autowired private AuthService authService;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        // 加上登入邏輯
        System.out.println("登入：" + username);

        if(authService.authenticate(username, password)){
            errorLabel.setText("");
            try{
                SceneManager.switchScene("/fxml/main.fxml","/styles/main.css","Main Page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            errorLabel.setText("Invalid credentials");
        }
    }

    @FXML
    private void handleSignUp() {
        // 導向註冊頁面
        System.out.println("前往註冊頁");
    }
    public LoginController(){
        System.out.println("loginController有被spring建立" );
        System.out.println(getClass().getResource("/styles/themes/login.css"));
    }
    @FXML
    private void handleSignUp(ActionEvent event) {
        try {
            SceneManager.switchScene("/fxml/enroll.fxml", "/styles/enroll.css", "Sign Up");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
