package event;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.HashSet;

public class KeyHandler {
    private final HashMap<String, HashSet<KeyCode>> registeredKeys = new HashMap<>();
    private final HashSet<KeyCode> pressedKeys = new HashSet<>();
    
    public KeyHandler(Scene scene) {
        scene.setOnKeyPressed((event) -> {
            KeyCode code = event.getCode();
            pressedKeys.add(code);
        });
        
        scene.setOnKeyReleased((event) -> {
            KeyCode code = event.getCode();
            pressedKeys.remove(code);
        });
    }
    
    public void registerKey(String groupId, KeyCode keyCode) {
        HashSet<KeyCode> arr = registeredKeys.computeIfAbsent(groupId, k -> new HashSet<>());
        arr.add(keyCode);
    }
    
    public boolean isKeyPressed(String groupId) {
        HashSet<KeyCode> group = registeredKeys.get(groupId);
        if (group == null) return false;
        for (KeyCode keyCode : group) {
            if (pressedKeys.contains(keyCode)) return true;
        }
        return false;
    }
}
