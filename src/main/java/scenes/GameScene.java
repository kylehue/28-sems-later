package scenes;

import game.Game;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import main.GameApplication;

public class GameScene extends GameApplicationScene {
    private final Game game = new Game();
    public GameScene(GameApplication gameApplication, String sceneId) {
        super(gameApplication, sceneId);
        
        game.initEventHandlers(scene);
        
        // Add canvas to game scene
        Canvas canvas = game.getCanvas();
        defaultRoot.getChildren().add(canvas);
        canvas.widthProperty().bind(scene.widthProperty());
        canvas.heightProperty().bind(scene.heightProperty());
    }
    
    public Game getGame() {
        return game;
    }
}
