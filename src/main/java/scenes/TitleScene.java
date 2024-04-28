package scenes;

import game.Game;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import main.GameApplication;
import utils.Common;
import utils.Parallax;

public class TitleScene extends GameApplicationScene {
    public TitleScene(GameApplication gameApplication, String sceneId) {
        super(gameApplication, sceneId);
        
        MediaPlayer music = new MediaPlayer(Common.loadMedia("/music/menu.mp3"));
        music.setCycleCount(Integer.MAX_VALUE);
        music.setVolume(0.5);
        music.play();
        
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
        
        // Setup main layout
        GridPane mainLayout = new GridPane();
        Common.setupGridPane(mainLayout, 1, 2);
        root.getChildren().add(mainLayout);
        mainLayout.getColumnConstraints().get(0).setPercentWidth(50);
        mainLayout.getColumnConstraints().get(1).setPercentWidth(50);
        
        // Setup layout on left
        GridPane leftLayout = new GridPane();
        Common.setupGridPane(leftLayout, 2, 1);
        mainLayout.add(leftLayout, 0, 0);
        leftLayout.getRowConstraints().get(0).setPercentHeight(70);
        leftLayout.getRowConstraints().get(1).setPercentHeight(30);
        
        // Setup logo
        ImageView imageView = new ImageView(
            Common.resampleImage(
                Common.loadImage("/brand/logo.png"),
                8
            )
        );
        GridPane.setHalignment(imageView, HPos.CENTER);
        GridPane.setValignment(imageView, VPos.CENTER);
        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(root.widthProperty().divide(2.5));
        leftLayout.add(imageView, 0, 0);
        
        // Setup start button
        Button startButton = new Button("Start");
        GridPane.setHalignment(startButton, HPos.CENTER);
        GridPane.setValignment(startButton, VPos.TOP);
        leftLayout.add(startButton, 0, 1);
        
        startButton.setOnAction(e -> {
            startButton.setDisable(true);
            startButton.setText("Loading...");
            Game game = gameApplication.getGameScene().getGame();
            game.startGameAsync().setOnSucceeded((t) -> {
                gameApplication.getSceneManager().setScene(
                    gameApplication.getGameScene().getSceneId()
                );
            });
        });
        
        gameApplication.getSceneManager().currentSceneProperty().addListener(e -> {
            if (
                gameApplication
                    .getSceneManager()
                    .currentSceneProperty()
                    .get()
                    .equals("title")
            ) {
                parallax.start();
                music.play();
                startButton.setDisable(false);
                startButton.setText("Start");
            } else {
                parallax.pause();
                music.stop();
            }
        });
    }
}
