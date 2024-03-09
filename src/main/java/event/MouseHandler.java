package event;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import utils.Vector;

public class MouseHandler {
    private final BooleanProperty mouseLeftPressed = new SimpleBooleanProperty(false);
    private final BooleanProperty mouseRightPressed = new SimpleBooleanProperty(false);
    private final Vector position = new Vector();
    
    public MouseHandler(Scene scene) {
        scene.setOnMouseMoved(event -> {
            this.position.set(
                (float) event.getSceneX(),
                (float) event.getSceneY()
            );
        });
        
        scene.setOnMouseDragged(event -> {
            this.position.set(
                (float) event.getSceneX(),
                (float) event.getSceneY()
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
