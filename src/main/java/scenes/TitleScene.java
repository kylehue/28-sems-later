package scenes;

import game.Game;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import main.GameApplication;
import utils.Common;
import utils.Parallax;

public class TitleScene extends GameApplicationScene {
    private final MediaPlayer music;
    private final Parallax parallax;
    private final Button startButton;
    
    public TitleScene(GameApplication gameApplication, Object sceneKey) {
        super(gameApplication, sceneKey);
        
        this.startButton = new Button();
        this.parallax = new Parallax(scene);
        this.music = new MediaPlayer(Common.loadMedia("/music/menu.mp3"));
        this.music.setCycleCount(Integer.MAX_VALUE);
        this.music.setVolume(0.5);
        this.music.play();
        
        gameApplication.getSceneManager().currentSceneProperty().addListener(e -> {
            if (
                gameApplication
                    .getSceneManager()
                    .getCurrentScene()
                    == sceneKey
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
        
        this.setupParallax();
        this.setupMenu();
    }
    
    private void setupParallax() {
        defaultRoot.getChildren().add(parallax.getCanvas());
        parallax.getCanvas().widthProperty().bind(defaultRoot.widthProperty());
        parallax.getCanvas().heightProperty().bind(defaultRoot.heightProperty());
        parallax.addLayer(0, Common.loadImage("/backgrounds/parallax/main/layer-6.png"));
        parallax.addLayer(1, Common.loadImage("/backgrounds/parallax/main/layer-5.png"));
        parallax.addLayer(2, Common.loadImage("/backgrounds/parallax/main/layer-4.png"));
        parallax.addLayer(3, Common.loadImage("/backgrounds/parallax/main/layer-3.png"));
        parallax.addLayer(4, Common.loadImage("/backgrounds/parallax/main/layer-2.png"));
        parallax.addLayer(5, Common.loadImage("/backgrounds/parallax/main/layer-1.png"));
        parallax.start();
    }
    
    private void setupMenu() {
        VBox parent = new VBox();
        defaultRoot.getChildren().add(parent);
        parent.setSpacing(80);
        parent.prefWidthProperty().bind(defaultRoot.widthProperty().divide(2.5));
        parent.maxWidthProperty().bind(defaultRoot.widthProperty().divide(2.5));
        parent.minWidthProperty().bind(defaultRoot.widthProperty().divide(2.5));
        StackPane.setAlignment(parent, Pos.CENTER_LEFT);
        parent.setAlignment(Pos.CENTER);
        
        // Setup logo
        ImageView imageView = new ImageView(Common.loadImage("/brand/logo.png"));
        parent.getChildren().add(imageView);
        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(parent.heightProperty().divide(2.5));
        
        // Setup start button
        parent.getChildren().add(startButton);
        
        startButton.setOnAction(e -> {
            startButton.setDisable(true);
            startButton.setText("Loading...");
            Game game = gameApplication.getGameScene().getGame();
            game.startGameAsync().setOnSucceeded((t) -> {
                gameApplication.getSceneManager().setScene(
                    gameApplication.getGameScene().getSceneKey()
                );
            });
        });
    }
}
