package com.demo.util;

import com.demo.service.MapMarkerService;
import javafx.application.Platform;
import javafx.scene.web.WebEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapMarkerManager {

    @Autowired
    private MapMarkerService markerService;

    private WebEngine webEngine;

    public void setWebEngine(WebEngine webEngine) {
        this.webEngine = webEngine;
    }

    public void clearTemporaryMarker() {
        if (webEngine != null) {
            Platform.runLater(() -> {
                webEngine.executeScript("clearLastMarker();");
            });
        }
    }

    public void refreshMapMarkers() {
        if (webEngine != null) {
            Platform.runLater(() -> {
                try {
                    // 刷新用户标记
                    markerService.refreshUserMarkers();
                    // 清除地图上现有标记并重新添加
                    webEngine.executeScript("clearAllMarkers();");
                    webEngine.executeScript(markerService.generateMarkersJavaScript());
                } catch (Exception e) {
                    System.err.println("刷新地圖標記失敗: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }
    }

    public void addMarkersToMap() {
        if (webEngine != null) {
            Platform.runLater(() -> {
                try {
                    markerService.refreshUserMarkers();
                    webEngine.executeScript(markerService.generateMarkersJavaScript());
                } catch (Exception e) {
                    System.err.println("新增標記失敗: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }
    }
}