package com.demo.controller;

import javafx.scene.web.*;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.stage.*;
import javafx.fxml.FXML;
import javafx.application.Platform;
import com.demo.util.SceneManager;
import java.io.IOException;
import java.net.URL;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

@Component
public class MainController {

    @FXML
    private WebView mapWebView;


    @FXML
    private WebEngine webEngine;

    @FXML
    public void initialize() {
        URL url = getClass().getResource("/static/map.html");
        if (url != null) {
            webEngine = mapWebView.getEngine();
            webEngine.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            webEngine.setJavaScriptEnabled(true); // Enable JavaScript
            webEngine.load(url.toExternalForm());
            // Debug URL
            System.out.println("Loading map from URL: " + url.toExternalForm());
        } else {
            System.err.println("Map HTML not found.");
        }

        // Handle WebView size binding
        mapWebView.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                mapWebView.prefWidthProperty().bind(newScene.widthProperty());
                mapWebView.prefHeightProperty().bind(newScene.heightProperty());
            }
        });

        // Debug JavaScript errors
        webEngine.setOnError(event -> {
            System.err.println("JavaScript Error: " + event.getMessage());
        });
        webEngine.getLoadWorker().exceptionProperty().addListener((obs, oldException, newException) -> {
            if (newException != null) {
                newException.printStackTrace();
            }
        });

        // Debug load state
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
//            System.out.println(webEngine.g);
            if (newState == Worker.State.FAILED) {
                System.err.println("Failed to load URL: " + webEngine.getLocation());
            } else if (newState == Worker.State.SUCCEEDED) {
                System.out.println("Successfully loaded URL: " + webEngine.getLocation());
            }
        });

    }

    @FXML
    private void handleLogout(){
        Platform.runLater(() -> {
            try {
                SceneManager.switchScene("/fxml/login.fxml", "/styles/main.css", "Login");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    private void openDescriptionWindow() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/description.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Description Area");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}