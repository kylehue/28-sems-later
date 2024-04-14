package entity;

import javafx.scene.canvas.GraphicsContext;
import utils.Vector;

/**
 * Interface for any moving and drawable object.
 */
public interface Thing {
    Vector getPosition();
    Vector getRenderPosition();
    int getZIndex();
    boolean isSeeThrough();
    void render(GraphicsContext graphicsContext, float alpha);
}
