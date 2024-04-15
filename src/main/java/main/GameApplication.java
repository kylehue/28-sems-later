package main;

import javafx.stage.Stage;
import scenes.GameScene;
import scenes.TitleScene;
import utils.SceneManager;

public class GameApplication {
    private final Stage stage;
    private final SceneManager sceneManager;
    private final GameScene gameScene;
    private final TitleScene titleScene;
    
    public GameApplication(Stage stage) {
        this.stage = stage;
        this.sceneManager = new SceneManager(stage);
        
        // Setup window
        stage.setTitle("Epic game");
        stage.setWidth(1280);
        stage.setHeight(720);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
        
        // Create the game application's scenes
        titleScene = new TitleScene(this, "title");
        gameScene = new GameScene(this, "game");
        
        // Set main scene to title screen
        sceneManager.setScene("title");
    }
    
    public GameScene getGameScene() {
        return gameScene;
    }
    
    public TitleScene getTitleScene() {
        return titleScene;
    }
    
    public SceneManager getSceneManager() {
        return sceneManager;
    }
    
    public Stage getStage() {
        return stage;
    }
}
