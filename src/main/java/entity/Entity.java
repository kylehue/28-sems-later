package entity;

import javafx.scene.canvas.GraphicsContext;
import utils.Vector;

public abstract class Entity {
    private final Vector position = new Vector();
    private final Vector velocity = new Vector();
    
    public Vector getPosition() {
        return position;
    }
    
    public Vector getVelocity() {
        return velocity;
    }
    
    // to be overridden
    public void render(GraphicsContext ctx) {
    
    }
    
    // to be overridden
    public void update(float deltaTime) {
    
    }
}
