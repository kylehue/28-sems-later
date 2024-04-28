package main;

import javafx.stage.Stage;
import scenes.GameScene;
import scenes.TitleScene;
import utils.Common;
import utils.SceneManager;

public class GameApplication {
    private final Stage stage;
    private final SceneManager sceneManager;
    private final GameScene gameScene;
    private final TitleScene titleScene;
    
    public enum Scene {
        TITLE,
        GAME
    }
    
    public GameApplication(Stage stage) {
        this.stage = stage;
        this.sceneManager = new SceneManager(stage);
        
        // Setup window
        stage.getIcons().add(Common.loadImage("/brand/head.png"));
        stage.setTitle("28 Sems Later");
        stage.setWidth(1280);
        stage.setHeight(720);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
        
        // Create the game application's scenes
        titleScene = new TitleScene(this, Scene.TITLE);
        gameScene = new GameScene(this, Scene.GAME);
        
        // Set main scene to title screen
        sceneManager.setScene(Scene.TITLE);
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
