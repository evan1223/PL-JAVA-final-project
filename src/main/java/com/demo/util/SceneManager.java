package com.demo.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import java.io.IOException;

public class SceneManager {

    private static ApplicationContext context;
    private static Stage primaryStage;

    public static void setAppContext(ApplicationContext ctx) {
        context = ctx;
    }
    public static void setStage(Stage stage) {
        primaryStage = stage;
    }
    public static void switchScene(String fxmlPath, String cssPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
        loader.setControllerFactory(context::getBean);
        Scene scene = new Scene(loader.load());
        if (cssPath != null) {
            scene.getStylesheets().add(SceneManager.class.getResource(cssPath).toExternalForm());
        }
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
    }
}
