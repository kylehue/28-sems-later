package event;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.HashSet;

public class KeyHandler extends EventHandler {
    private final HashMap<Object, HashSet<KeyCode>> registeredKeys = new HashMap<>();
    private final HashMap<Object, BooleanProperty> registeredKeyProperties = new HashMap<>();
    
    @Override
    protected void handleListeners(Scene scene) {
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
    
    public void registerKey(Object groupId, KeyCode keyCode) {
        HashSet<KeyCode> arr = registeredKeys.computeIfAbsent(groupId, k -> new HashSet<>());
        arr.add(keyCode);
        registeredKeyProperties.putIfAbsent(groupId, new SimpleBooleanProperty());
    }
    
    public boolean isKeyPressed(Object groupId) {
        return registeredKeyProperties.get(groupId).get();
    }
    
    public BooleanProperty getKeyPressedProperty(Object groupId) {
        BooleanProperty keyPressedProperty = registeredKeyProperties.get(groupId);
        if (keyPressedProperty == null) {
            keyPressedProperty = new SimpleBooleanProperty();
            registeredKeyProperties.put(groupId, keyPressedProperty);
        }
        
        return keyPressedProperty;
    }
}
