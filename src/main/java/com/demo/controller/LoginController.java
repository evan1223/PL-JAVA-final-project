package com.demo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import com.demo.util.SceneManager;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.demo.service.AuthService;

import java.io.IOException;

@Component
public class LoginController {

    @FXML
    private TextField inputUsername;

    @FXML
    private PasswordField inputPassword;

    @FXML
    private Label errorLabel;

    @Autowired
    private AuthService authService;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = inputUsername.getText();
        String password = inputPassword.getText();

        if (authService.authenticate(username, password)) {
            errorLabel.setText("");
            try {
                SceneManager.switchScene("/fxml/main.fxml", "/styles/main.css", "Main Page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Invalid credentials");
        }
    }

    @FXML
    private void handleGoToEnroll(ActionEvent event) {
        try {
            SceneManager.switchScene("/fxml/enroll.fxml", "/styles/main.css", "Sign Up");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
