package scenes;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import main.GameApplication;

public abstract class GameApplicationScene {
    protected final GameApplication gameApplication;
    protected final String sceneId;
    protected final Scene scene;
    protected final StackPane defaultRoot = new StackPane();
    
    public GameApplicationScene(GameApplication gameApplication, String sceneId) {
        this.gameApplication = gameApplication;
        this.sceneId = sceneId;
        this.scene = new Scene(defaultRoot);
        this.scene.getStylesheets().add(
            getClass().getResource("/styles/global.css").toExternalForm()
        );
        
        // register
        gameApplication.getSceneManager().registerScene(this.sceneId, scene);
    }
    
    public String getSceneId() {
        return sceneId;
    }
    
    public Scene getScene() {
        return scene;
    }
}
