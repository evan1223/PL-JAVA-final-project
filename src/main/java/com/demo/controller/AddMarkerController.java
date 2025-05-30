package com.demo.controller;

import com.demo.util.MapMarkerManager;
import com.demo.service.MapMarkerService;
import com.demo.util.MapMarker;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddMarkerController {

    private double latitude;
    private double longitude;

    @FXML
    private TextField markerNameField;

    @FXML
    private TextArea descriptionTextArea;

    @Autowired
    private MapMarkerService markerService;

    private Stage stage;

    private MapMarkerManager markerManager;

    public void setMarkerManager(MapMarkerManager markerManager) {
        this.markerManager = markerManager;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @FXML
    private void handleSaveMarker() {
        try {
            String name = markerNameField.getText().trim();
            String description = descriptionTextArea.getText().trim();

            if (name.isEmpty()) {
                System.out.println("標記名稱不能為空");
                return;
            }

            MapMarker marker = new MapMarker(latitude, longitude, name, description, true);
            markerService.saveMarkerToDatabase(marker, description);

            markerManager.clearTemporaryMarker();
            markerManager.refreshMapMarkers();
            stage.close();

        } catch (Exception e) {
            System.out.println("保存標記失敗: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        markerManager.clearTemporaryMarker();
        stage.close();
    }
}
