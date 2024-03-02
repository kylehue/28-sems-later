package utils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;

public class MouseHandler {
    private final BooleanProperty mouseLeftPressed = new SimpleBooleanProperty(false);
    private final BooleanProperty mouseRightPressed = new SimpleBooleanProperty(false);
    private final Vector position = new Vector();
    
    public MouseHandler(Scene scene) {
        scene.setOnMouseMoved(event -> {
            this.position.set(
                event.getSceneX(),
                event.getSceneY()
            );
        });
        
        scene.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                mouseLeftPressed.set(true);
            } else if (event.isSecondaryButtonDown()) {
                mouseRightPressed.set(true);
            }
        });
        
        scene.setOnMouseReleased(event -> {
            mouseLeftPressed.set(false);
            mouseRightPressed.set(false);
        });
    }
    
    public boolean isMouseLeftPressed() {
        return mouseLeftPressed.get();
    }
    
    public boolean isMouseRightPressed() {
        return mouseRightPressed.get();
    }
    
    public BooleanProperty mouseLeftPressedProperty() {
        return mouseLeftPressed;
    }
    
    public BooleanProperty mouseRightPressedProperty() {
        return mouseRightPressed;
    }
    
    public Vector getPosition() {
        return position;
    }
}
