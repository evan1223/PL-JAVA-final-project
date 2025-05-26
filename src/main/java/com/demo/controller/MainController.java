package com.demo.controller;

import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.io.IOException;
import java.net.URL;

import javafx.scene.web.*;
import javafx.concurrent.Worker;
import javafx.stage.*;
import javafx.fxml.FXML;
import javafx.application.Platform;
import netscape.javascript.JSObject;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.demo.util.MapClickCallback;
import com.demo.util.SceneManager;
import com.demo.util.UserSession;
import com.demo.service.WeatherService;
import com.demo.util.MapMarkerManager;


@Component
public class MainController implements MapClickCallback {

    @FXML
    private Label weatherLabel;

    @FXML
    private Label temperatureLabel;

    @FXML
    private Label precipitationLabel;

    @FXML
    private WebView mapWebView;

    @FXML
    private WebEngine webEngine;

    @FXML
    private ImageView weatherIcon;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private MapMarkerManager markerManager;

    @Autowired
    private UserSession userSession;

    private Stage markerWindow;

    @FXML
    public void initialize() {
        if (!userSession.isLoggedIn()) {
            // immediately go back to login
            Platform.runLater(() -> {
                try {
                    SceneManager.switchScene("/fxml/login.fxml", "/styles/login.css", "Login");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return;
        }
        URL url = getClass().getResource("/static/map.html");
        if (url != null) {
            webEngine = mapWebView.getEngine();
            webEngine.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            webEngine.setJavaScriptEnabled(true); // Enable JavaScript
            webEngine.load(url.toExternalForm());
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

        // Debug load state
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                // 设置Java连接器
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaConnector", this);

                // 设置WebEngine到markerManager
                markerManager.setWebEngine(webEngine);
                markerManager.addMarkersToMap();
            }
        });
        loadWeatherData();
    }

    @FXML
    private void handleLogout() {
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
                SceneManager.openDescriptionWindow();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadWeatherData() {
        new Thread(() -> {
            try {
                Map<String, Object> data = weatherService.getWeatherData();
                int weatherCode = (int) data.get("weatherCode");
                double temp = (double) data.get("temperature");
                int precip = (int) data.get("precipitation");
                String weatherDesc = weatherService.getWeatherDescription(weatherCode);

                Platform.runLater(() -> {
                    weatherLabel.setText("天氣: " + weatherDesc);
                    temperatureLabel.setText(String.format("%.1f°C", temp));
                    precipitationLabel.setText("降雨" + precip + "%");
                    updateWeatherIcon(weatherCode);
                });
            } catch (Exception e) {
                Platform.runLater(() -> weatherLabel.setText("無法取得當前天氣資料"));
                e.printStackTrace();
            }
        }).start();
    }

    private void updateWeatherIcon(int weatherCode) {
        String iconPath;
        if (weatherCode == 0) {
            iconPath = "/icons/sunny.png";
        } else if (weatherCode >= 1 && weatherCode <= 3) {
            iconPath = "/icons/cloudy.png";
        } else if (weatherCode >= 61) {
            iconPath = "/icons/rainy.png";
        } else {
            iconPath = "/icons/unknown.png";
        }
        weatherIcon.setImage(new Image(getClass().getResourceAsStream(iconPath)));
    }

    // 使用markerManager代替markerService
    private void addMarkersToMap() {
        Platform.runLater(() -> {
            try {
                markerManager.addMarkersToMap();
            } catch (Exception e) {
                System.err.println("Failed to add markers: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    @Override
    public boolean isMarkerWindowOpen() {
        return markerWindow != null && markerWindow.isShowing();
    }

    @Override
    public void onMapClick(double latitude, double longitude) {
        System.out.println("地圖被點擊：緯度=" + latitude + ", 經度=" + longitude);

        // open the marker window with the clicked coordinates
        if (isMarkerWindowOpen()) {
            Platform.runLater(() -> {
                Scene scene = markerWindow.getScene();
                if (scene != null) {
                    AddMarkerController controller = (AddMarkerController) scene.getUserData();
                    if (controller != null) {
                        // 更新控制器中的坐标
                        controller.setCoordinates(latitude, longitude);
                        System.out.println("已更新標記坐標: " + latitude + ", " + longitude);
                    }
                }
            });
        }
    }

    @FXML
    // 删除@Override注解，因为这不是接口中定义的方法
    public void openAddMarkerWindow() {
        Platform.runLater(() -> {
            try {
                // 需要更新SceneManager中的方法定义以接受markerManager参数
                markerWindow = SceneManager.openAddMarkerWindow(24.9866487, 121.5765365);

                // get the controller from the scene and set the markerManager
                if (markerWindow != null) {
                    Scene scene = markerWindow.getScene();
                    if (scene != null) {
                        AddMarkerController controller = (AddMarkerController) scene.getUserData();
                        if (controller != null) {
                            controller.setMarkerManager(markerManager);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("無法打開新增標記視窗: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}