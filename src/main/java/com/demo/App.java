package com.demo;

import javafx.application.Application;
import javafx.stage.Stage;
import com.demo.util.SceneManager;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class App extends Application {

    private ConfigurableApplicationContext springContext;

    @Override
    public void init() {
        // Start Spring Boot context
        springContext = new SpringApplicationBuilder(App.class).run();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneManager.setAppContext(springContext);
        SceneManager.setStage(primaryStage);

        System.out.println("Before switchScene");
        SceneManager.switchScene("/fxml/login.fxml", "/styles/login.css", "Login Page");
        System.out.println("After switchScene");

        primaryStage.show();
    }


    @Override
    public void stop() {
        springContext.close();
    }

    public static void main(String[] args) {
        launch(args);
        //never call init(), start(), or stop() yourself — JavaFX does it automatically once launch() is invoked.
    }
}
