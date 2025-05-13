package com.demo.controller;

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
public class EnrollController {

    @FXML
    private TextField newUsername;

    @FXML
    private PasswordField newPassword;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private Label enrollErrorLabel;

    @Autowired
    private AuthService authService;

    @FXML
    private void handleEnroll(ActionEvent event) {
        String username = newUsername.getText();
        String password = newPassword.getText();
        String confirm = confirmPassword.getText();

        if (!password.equals(confirm)) {
            enrollErrorLabel.setText("Passwords do not match.");
            return;
        }

        boolean success = authService.register(username, password);
        if (success) {
            try {
                SceneManager.switchScene("/fxml/login.fxml", "/styles/main.css", "Login");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            enrollErrorLabel.setText("Username already exists.");
        }
    }

    @FXML
    private void handleGoToLogin(ActionEvent event) {
        try {
            SceneManager.switchScene("/fxml/login.fxml", "/styles/main.css", "Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
