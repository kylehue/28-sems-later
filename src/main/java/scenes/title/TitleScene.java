package scenes.title;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import main.GameApplication;
import scenes.GameApplicationScene;
import utils.LayoutUtils;
import utils.Parallax;

public class TitleScene extends GameApplicationScene {
    public TitleScene(GameApplication gameApplication, String sceneId) {
        super(gameApplication, sceneId);
        
        // Set up the scene's root
        StackPane root = new StackPane();
        this.getDefaultRoot().getChildren().add(root);
        root.minWidthProperty().bind(this.getDefaultRoot().widthProperty());
        root.maxWidthProperty().bind(this.getDefaultRoot().widthProperty());
        root.prefWidthProperty().bind(this.getDefaultRoot().widthProperty());
        root.minHeightProperty().bind(this.getDefaultRoot().heightProperty());
        root.maxHeightProperty().bind(this.getDefaultRoot().heightProperty());
        root.prefHeightProperty().bind(this.getDefaultRoot().heightProperty());
        
        // Set up background (parallax)
        Parallax parallax = new Parallax(this.getScene());
        parallax.addLayer(0, LayoutUtils.loadImage("/backgrounds/parallax/main/layer-6.png"));
        parallax.addLayer(1, LayoutUtils.loadImage("/backgrounds/parallax/main/layer-5.png"));
        parallax.addLayer(2, LayoutUtils.loadImage("/backgrounds/parallax/main/layer-4.png"));
        parallax.addLayer(3, LayoutUtils.loadImage("/backgrounds/parallax/main/layer-3.png"));
        parallax.addLayer(4, LayoutUtils.loadImage("/backgrounds/parallax/main/layer-2.png"));
        parallax.addLayer(5, LayoutUtils.loadImage("/backgrounds/parallax/main/layer-1.png"));
        parallax.startLoop();
        parallax.getCanvas().widthProperty().bind(root.widthProperty());
        parallax.getCanvas().heightProperty().bind(root.heightProperty());
        root.getChildren().add(parallax.getCanvas());
        
        // Setup buttons
        GridPane btnRoot = new GridPane();
        LayoutUtils.setupGridPane(btnRoot, 2, 1);
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
            gameApplication.getGameScene().startGame().setOnSucceeded((t) -> {
                gameApplication.getSceneManager().setScene(
                    gameApplication.getGameScene().getSceneId()
                );
                parallax.pauseLoop();
            });
        });
    }
}
