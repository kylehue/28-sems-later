package colliders;

import javafx.scene.canvas.GraphicsContext;
import utils.GameUtils;
import utils.Vector;

public abstract class Collider {
    private final String id = GameUtils.generateId();
    private Vector position = new Vector();
    private Vector velocity = new Vector();
    private boolean isStatic = false;
    private double width = 15;
    private double height = 25;
    
    public void setBoundSize(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    public double getWidth() {
        return width;
    }
    
    public double getHeight() {
        return height;
    }
    
    public boolean isStatic() {
        return isStatic;
    }
    
    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }
    
    public String getId() {
        return id;
    }
    
    public Vector getPosition() {
        return position;
    }
    
    public Vector getVelocity() {
        return velocity;
    }
    
    protected void update(double deltaTime) {
        this.position.add(this.velocity);
    }
    
    // to be overridden
    public void render(GraphicsContext ctx) {
    
    }
    
    // to be overridden
    protected void resolveCollision(Collider otherCollider) {
    
    }
}
