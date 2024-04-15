package game.entity;

import javafx.scene.canvas.GraphicsContext;
import game.utils.Vector;

/**
 * Interface for any moving and drawable object.
 */
public interface Drawable {
    /**
     * Actual position of the object.
     */
    Vector getPosition();
    
    /**
     * This is where the Y-sorting will be based on.
     */
    Vector getRenderPosition();
    
    /**
     * This is where the Z-sorting will be based on.
     */
    int getZIndex();
    
    /**
     * Whether the object should be transparent when it's close to
     * the camera's center view.
     */
    boolean isSeeThrough();
    
    /**
     * Rendering method of the object.
     */
    void render(GraphicsContext graphicsContext, float alpha);
}
