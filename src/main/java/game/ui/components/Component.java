package game.ui.components;

import game.ui.UI;
import javafx.scene.canvas.GraphicsContext;

public abstract class Component {
    protected boolean isVisible = false;
    public abstract void render(GraphicsContext graphicsContext);

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
    
    public boolean isVisible() {
        return isVisible;
    }
}
