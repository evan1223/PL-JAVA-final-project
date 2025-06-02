package com.demo.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.net.URL;

import com.demo.controller.AddMarkerController;

public class SceneManager {

    private static ApplicationContext context;
    private static Stage primaryStage;

    public static void setAppContext(ApplicationContext ctx) {
        context = ctx;
    }

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }
    public static Stage getPrimaryStage() {
        return primaryStage;
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

    // open a modal window with the specified FXML path and title
    public static Stage openModalWindow(String fxmlPath, String title) throws IOException {
        URL fxmlUrl = SceneManager.class.getResource(fxmlPath);
        if (fxmlUrl == null) {
            throw new IOException("FXML file not found: " + fxmlPath);
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);

        // if spring context is set, use it to create controllers
        if (context != null) {
            loader.setControllerFactory(clazz -> context.getBean(clazz));
        }

        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(primaryStage);
        stage.show();

        return stage;
    }

    // open a modal window for the description area
    public static Stage openDescriptionWindow() throws IOException {
        return openModalWindow("/fxml/description.fxml", "Description Area");
    }

    public static Stage openAddMarkerWindow(double latitude, double longitude) throws IOException {
        URL fxmlUrl = SceneManager.class.getResource("/fxml/addMarker.fxml");
        if (fxmlUrl == null) {
            throw new IOException("FXML file not found: /fxml/addMarker.fxml");
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);

        if (context != null) {
            loader.setControllerFactory(clazz -> context.getBean(clazz));
        }

        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("新增標記");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.NONE);
        stage.initOwner(primaryStage);

        // 设置经纬度
        AddMarkerController controller = loader.getController();
        controller.setStage(stage);
        controller.setCoordinates(latitude, longitude);

        // 将控制器保存到Scene的userData中，以便MainController可以获取到它
        stage.getScene().setUserData(controller);

        stage.show();

        return stage;
    }
}
