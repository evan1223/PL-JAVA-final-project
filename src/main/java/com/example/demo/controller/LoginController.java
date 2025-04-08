package com.example.demo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import org.springframework.stereotype.Component;

@Component
public class LoginController {

    @FXML private TextField username;
    @FXML private PasswordField password;

    @FXML
    public void handleLogin(ActionEvent event) throws Exception {
        if ("admin".equals(username.getText()) && "password".equals(password.getText())) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
            Stage stage = (Stage) username.getScene().getWindow();
            stage.setScene(scene);
        } else {
            username.setText("");
            password.setText("");
        }
    }
}
