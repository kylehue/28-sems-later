package scenes;

import game.Game;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import main.GameApplication;
import utils.Common;
import utils.Parallax;

public class TitleScene extends GameApplicationScene {
    public TitleScene(GameApplication gameApplication, String sceneId) {
        super(gameApplication, sceneId);
        
        // Set up the scene's root
        StackPane root = new StackPane();
        defaultRoot.getChildren().add(root);
        root.minWidthProperty().bind(defaultRoot.widthProperty());
        root.maxWidthProperty().bind(defaultRoot.widthProperty());
        root.prefWidthProperty().bind(defaultRoot.widthProperty());
        root.minHeightProperty().bind(defaultRoot.heightProperty());
        root.maxHeightProperty().bind(defaultRoot.heightProperty());
        root.prefHeightProperty().bind(defaultRoot.heightProperty());
        
        // Set up background (parallax)
        Parallax parallax = new Parallax(scene);
        parallax.addLayer(0, Common.loadImage("/backgrounds/parallax/main/layer-6.png"));
        parallax.addLayer(1, Common.loadImage("/backgrounds/parallax/main/layer-5.png"));
        parallax.addLayer(2, Common.loadImage("/backgrounds/parallax/main/layer-4.png"));
        parallax.addLayer(3, Common.loadImage("/backgrounds/parallax/main/layer-3.png"));
        parallax.addLayer(4, Common.loadImage("/backgrounds/parallax/main/layer-2.png"));
        parallax.addLayer(5, Common.loadImage("/backgrounds/parallax/main/layer-1.png"));
        parallax.start();
        parallax.getCanvas().widthProperty().bind(root.widthProperty());
        parallax.getCanvas().heightProperty().bind(root.heightProperty());
        root.getChildren().add(parallax.getCanvas());
        
        // Setup buttons
        GridPane btnRoot = new GridPane();
        Common.setupGridPane(btnRoot, 2, 1);
        root.getChildren().add(btnRoot);
        
        // Set up stuff inside the scene
        Label title = new Label("cool epic gam");
        GridPane.setHalignment(title, HPos.CENTER);
        GridPane.setValignment(title, VPos.CENTER);
        btnRoot.add(title, 0, 0);
        
        Button startButton = new Button("strat");
        GridPane.setHalignment(startButton, HPos.CENTER);
        GridPane.setValignment(startButton, VPos.CENTER);
        btnRoot.add(startButton, 0, 1);
        
        startButton.setOnAction(e -> {
            startButton.setDisable(true);
            startButton.setText("Loading...");
            Game game = gameApplication.getGameScene().getGame();
            game.startGameAsync().setOnSucceeded((t) -> {
                parallax.pause();
                gameApplication.getSceneManager().setScene(
                    gameApplication.getGameScene().getSceneId()
                );
            });
        });
    }
}
