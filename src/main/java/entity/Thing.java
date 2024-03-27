package entity;

import javafx.scene.canvas.GraphicsContext;
import utils.Vector;

/**
 * Interface for any moving and drawable object.
 */
public interface Thing {
    Vector getPosition();
    int getZIndex();
    void render(GraphicsContext graphicsContext);
}
