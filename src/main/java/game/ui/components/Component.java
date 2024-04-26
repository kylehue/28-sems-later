package game.ui.components;

import game.ui.UI;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.canvas.GraphicsContext;

public abstract class Component {
    private final BooleanProperty isVisible = new SimpleBooleanProperty(false);
    public abstract void render(GraphicsContext graphicsContext);

    public void setVisible(boolean visible) {
        isVisible.set(visible);
    }
    
    public boolean isVisible() {
        return isVisible.get();
    }
    
    public BooleanProperty isVisibleProperty() {
        return isVisible;
    }
    
    public boolean isBusy() {
        return false;
    }
}
