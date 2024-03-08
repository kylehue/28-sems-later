package colliders;

import javafx.scene.canvas.GraphicsContext;
import utils.GameUtils;
import utils.Vector;

import java.util.HashSet;

public abstract class Collider {
    private final String id = GameUtils.generateId();
    private final Vector position = new Vector();
    private final Vector oldPosition = new Vector();
    private final Vector velocity = new Vector();
    private final Vector oldVelocity = new Vector();
    private boolean isStatic = false;
    private double width = 15;
    private double height = 25;
    private final HashSet<String> contacts = new HashSet<>();
    
    protected HashSet<String> getContacts() {
        return contacts;
    }
    
    public boolean isCollidingWith(Collider collider) {
        return contacts.contains(collider.getId());
    }
    
    public boolean isColliding() {
        return !contacts.isEmpty();
    }
    
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
    
    public Vector getOldPosition() {
        return oldPosition;
    }
    
    public Vector getVelocity() {
        return velocity;
    }
    
    public Vector getOldVelocity() {
        return oldVelocity;
    }
    
    protected void update(double deltaTime, ColliderWorld world) {
        Vector oldVelocity = this.velocity.clone();
        Vector oldPosition = this.position.clone();
        this.position.add(this.velocity);
        this.position.add(this.velocity.clone().scale(deltaTime));
        this.velocity.divide(world.getUpdateIterationCount());
        this.oldVelocity.set(oldVelocity);
        this.oldPosition.set(oldPosition);
    }
    
    // to be overridden
    public void render(GraphicsContext ctx) {
    
    }
    
    // to be overridden
    protected void resolveCollision(Collider otherCollider) {
    
    }
}
