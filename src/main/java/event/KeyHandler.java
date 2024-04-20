package event;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.HashSet;

public class KeyHandler {
    private final HashMap<String, HashSet<KeyCode>> registeredKeys = new HashMap<>();
    private final HashMap<String, BooleanProperty> registeredKeyProperties = new HashMap<>();
    
    public KeyHandler(Scene scene) {
        scene.setOnKeyPressed((event) -> {
            KeyCode code = event.getCode();
            registeredKeys.forEach((key, value) -> {
                if (value.contains(code)) {
                    registeredKeyProperties.get(key).set(true);
                }
            });
        });
        
        scene.setOnKeyReleased((event) -> {
            KeyCode code = event.getCode();
            registeredKeys.forEach((key, value) -> {
                if (value.contains(code)) {
                    registeredKeyProperties.get(key).set(false);
                }
            });
        });
    }
    
    public void registerKey(String groupId, KeyCode keyCode) {
        HashSet<KeyCode> arr = registeredKeys.computeIfAbsent(groupId, k -> new HashSet<>());
        arr.add(keyCode);
        registeredKeyProperties.put(groupId, new SimpleBooleanProperty());
    }
    
    public boolean isKeyPressed(String groupId) {
        return registeredKeyProperties.get(groupId).get();
    }
    
    public BooleanProperty getKeyPressedProperty(String groupId) {
        return registeredKeyProperties.get(groupId);
    }
}
