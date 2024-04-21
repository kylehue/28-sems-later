package event;

import javafx.scene.Scene;

public abstract class EventHandler {
    protected abstract void handleListeners(Scene scene);
    
    private boolean isListening = false;
    public void listen(Scene scene) {
        if (isListening) return;
        handleListeners(scene);
        isListening = true;
    }
}
