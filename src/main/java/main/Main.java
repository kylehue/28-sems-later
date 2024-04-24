package main;

import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        GameApplication gameApplication = new GameApplication(stage);
    }
    
    public static void main(String[] args) {
        launch();
    }
}