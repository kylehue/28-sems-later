package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        // setup window
        stage.setTitle("Sample Game");
        stage.setWidth(640);
        stage.setHeight(480);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
        
        // make the scene
        Pane pane = new Pane();
        Scene gameScene = new Scene(pane);
        stage.setScene(gameScene);
        
        // make the canvas
        Game game = new Game();
        game.startGameLoop();
        pane.getChildren().add(game.getCanvas());
        game.getCanvas().widthProperty().bind(gameScene.widthProperty());
        game.getCanvas().heightProperty().bind(gameScene.heightProperty());
        
        // GraphicsContext ctx = gameCanvas.getGraphicsContext2D(); // yan
        //
        //
        // Player player = new Player();
        // player.render(ctx);
        // player.update();
        
    }
    
    public static void main(String[] args) {
        launch();
    }
    
}