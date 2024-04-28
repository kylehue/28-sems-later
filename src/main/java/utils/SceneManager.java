package utils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;

public class SceneManager {
    private final Stage stage;
    private final HashMap<String, Scene> registeredScenes = new HashMap<>();
    private final StringProperty currentScene = new SimpleStringProperty();
    
    public SceneManager(Stage stage) {
        this.stage = stage;
    }
    
    /**
     * Registers a scene.
     * @param sceneId The id of the scene to register.
     * @param scene The scene to register.
     */
    public void registerScene(String sceneId, Scene scene) {
        registeredScenes.put(sceneId, scene);
    }
    
    /**
     * Creates and registers a scene.
     * @param sceneId The id of the scene to create and register.
     * @return The created scene.
     */
    public Scene createScene(String sceneId) {
        Pane sceneRoot = new Pane();
        Scene scene = new Scene(sceneRoot);
        registerScene(sceneId, scene);
        return scene;
    }
    
    /**
     * Changes the stage's current scene.
     * @param sceneId The id of the scene to set.
     */
    public void setScene(String sceneId) {
        Scene scene = this.getScene(sceneId);
        stage.setScene(scene);
        currentScene.set(sceneId);
    }
    
    /**
     * Retrieves a scene from registered scenes using its id. Throws an
     * error if the scene doesn't exist.
     * @param sceneId The id of the scene to get.
     * @return The retrieved scene.
     */
    public Scene getScene(String sceneId) {
        Scene scene = registeredScenes.get(sceneId);
        if (scene == null) {
            throw new Error("The scene '" + sceneId + "' does not exist.");
        }
        return scene;
    }
    
    public StringProperty currentSceneProperty() {
        return currentScene;
    }
}
