package main;

import javafx.application.Application;
import javafx.stage.Stage;
import utils.Common;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        GameApplication gameApplication = new GameApplication(stage);
        Common.loadFont("/fonts/PIXY.ttf", 14);
    }
    
    public static void main(String[] args) {
        launch();
    }
}