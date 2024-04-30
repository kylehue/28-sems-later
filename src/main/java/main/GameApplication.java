package main;

import javafx.application.Application;
import javafx.stage.Stage;
import scenes.GameScene;
import scenes.TitleScene;
import utils.Common;
import utils.SceneManager;

public class GameApplication extends Application {
    private Stage stage;
    private SceneManager sceneManager;
    private GameScene gameScene;
    private TitleScene titleScene;
    
    public enum Scene {
        TITLE,
        GAME
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
    
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.sceneManager = new SceneManager(stage);
        
        // Setup window
        stage.getIcons().add(Common.loadImage("/icon/48x48.png"));
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
    
    public static void execute() {
        Common.loadFont("/fonts/PIXY.ttf", 14);
        launch();
    }
}
