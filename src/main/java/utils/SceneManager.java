package utils;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;

public class SceneManager {
    private final Stage stage;
    private final HashMap<Object, Scene> registeredScenes = new HashMap<>();
    private final ObjectProperty<Object> currentScene = new SimpleObjectProperty();
    
    public SceneManager(Stage stage) {
        this.stage = stage;
    }
    
    /**
     * Registers a scene.
     * @param sceneKey The id of the scene to register.
     * @param scene The scene to register.
     */
    public void registerScene(Object sceneKey, Scene scene) {
        registeredScenes.put(sceneKey, scene);
    }
    
    /**
     * Creates and registers a scene.
     * @param sceneKey The id of the scene to create and register.
     * @return The created scene.
     */
    public Scene createScene(Object sceneKey) {
        Pane sceneRoot = new Pane();
        Scene scene = new Scene(sceneRoot);
        registerScene(sceneKey, scene);
        return scene;
    }
    
    /**
     * Changes the stage's current scene.
     * @param sceneKey The id of the scene to set.
     */
    public void setScene(Object sceneKey) {
        Scene scene = this.getScene(sceneKey);
        stage.setScene(scene);
        currentScene.set(sceneKey);
    }
    
    /**
     * Retrieves a scene from registered scenes using its id. Throws an
     * error if the scene doesn't exist.
     * @param sceneKey The id of the scene to get.
     * @return The retrieved scene.
     */
    public Scene getScene(Object sceneKey) {
        Scene scene = registeredScenes.get(sceneKey);
        if (scene == null) {
            throw new Error("The scene '" + sceneKey + "' does not exist.");
        }
        return scene;
    }
    
    public ObjectProperty<Object> currentSceneProperty() {
        return currentScene;
    }
    
    public Object getCurrentScene() {
        return currentScene.get();
    }
}
