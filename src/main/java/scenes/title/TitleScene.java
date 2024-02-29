package scenes.title;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import main.GameApplication;
import scenes.GameApplicationScene;
import scenes.game.GameScene;
import utils.LayoutUtils;

public class TitleScene extends GameApplicationScene {
    public TitleScene(GameApplication gameApplication, String sceneId) {
        super(gameApplication, sceneId);
        
        // Set up the scene's root
        GridPane root = new GridPane();
        LayoutUtils.setupGridPane(root, 2, 1);
        this.getDefaultRoot().getChildren().add(root);
        
        // Set up stuff inside the scene
        Label title = new Label("cool epic gam");
        GridPane.setHalignment(title, HPos.CENTER);
        GridPane.setValignment(title, VPos.CENTER);
        root.add(title, 0, 0);
        
        Button startButton = new Button("strat");
        GridPane.setHalignment(startButton, HPos.CENTER);
        GridPane.setValignment(startButton, VPos.CENTER);
        root.add(startButton, 0, 1);
        
        startButton.setOnAction(e -> {
            gameApplication.startGame();
        });
    }
}
