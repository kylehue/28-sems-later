package scenes;

import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import main.GameApplication;

public abstract class GameApplicationScene {
    protected final GameApplication gameApplication;
    protected final Object sceneKey;
    protected final Scene scene;
    protected final StackPane defaultRoot = new StackPane();
    
    public GameApplicationScene(GameApplication gameApplication, Object sceneKey) {
        this.gameApplication = gameApplication;
        this.sceneKey = sceneKey;
        this.scene = new Scene(defaultRoot);
        this.scene.getStylesheets().add(
            getClass().getResource("/styles/global.css").toExternalForm()
        );
        
        Image crosshairImage = utils.Common.loadImage("/weapons/crosshair.png");
        scene.setCursor(
            new ImageCursor(
                crosshairImage,
                crosshairImage.getWidth() / 2,
                crosshairImage.getHeight() / 2
            )
        );
        
        // register
        gameApplication.getSceneManager().registerScene(this.sceneKey, scene);
    }
    
    public Object getSceneKey() {
        return sceneKey;
    }
    
    public Scene getScene() {
        return scene;
    }
    
    public StackPane getDefaultRoot() {
        return defaultRoot;
    }
}
