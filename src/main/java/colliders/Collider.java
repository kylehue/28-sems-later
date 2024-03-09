package colliders;

import javafx.scene.canvas.GraphicsContext;
import utils.GameUtils;
import utils.Vector;

import java.util.HashSet;

public abstract class Collider {
    private final String id = GameUtils.generateId();
    private final Vector position = new Vector();
    private final Vector velocity = new Vector();
    private final Vector oldPosition = new Vector();
    private final Vector oldVelocity = new Vector();
    private float mass = 1;
    private boolean isStatic = false;
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
    
    public void setMass(float mass) {
        this.mass = mass;
    }
    
    public float getMass() {
        return mass;
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
    
    public Vector getOldPosition() {
        return oldPosition;
    }
    
    public Vector getOldVelocity() {
        return oldVelocity;
    }

    protected void update(float deltaTime) {
        Vector oldPosition = this.position.clone();
        Vector oldVelocity = this.velocity.clone();
        
        this.position.add(this.velocity);
        this.position.add(this.velocity.clone().scale(deltaTime));
        
        this.oldPosition.set(oldPosition);
        this.oldVelocity.set(oldVelocity);
    }
    
    // to be overridden
    public float getWidth() {
        return 0;
    }
    
    // to be overridden
    public float getHeight() {
        return 0;
    }
    
    // to be overridden
    public void render(GraphicsContext ctx) {
    
    }
    
    // to be overridden
    protected void resolveCollision(Collider otherCollider) {
    
    }
}
