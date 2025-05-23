package com.demo.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.net.URL;

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
        System.out.println("switchScene called with: " + fxmlPath);
        URL fxmlUrl = SceneManager.class.getResource(fxmlPath);
        if (fxmlUrl == null) {
            throw new IOException("FXML file not found: " + fxmlPath);
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        loader.setControllerFactory(clazz -> {
            System.out.println("Controller factory triggered: " + clazz.getName());
            Object bean = context.getBean(clazz);
            System.out.println("➡ 使用 Spring 建立 Controller: " + bean.getClass().getName());
            return bean;
        });

        Parent root = loader.load();

        Scene scene = new Scene(root);

        if (cssPath != null) {
            URL cssUrl = SceneManager.class.getResource(cssPath);
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("Warning: CSS file not found: " + cssPath);
            }
        }

        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
//        primaryStage.show();
    }
}
