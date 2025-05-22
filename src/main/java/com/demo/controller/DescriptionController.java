package com.demo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class DescriptionController {

    @FXML private TextArea descriptionTextArea;

    @FXML private VBox descriptionLogBox;

    @FXML
    private void handleSubmitDescription() {
        String text = descriptionTextArea.getText().trim();
        if (!text.isEmpty()) {
            Label label = new Label(text);
            label.setWrapText(true);
            descriptionLogBox.getChildren().add(0, label);
            descriptionTextArea.clear();
        }
    }
}
