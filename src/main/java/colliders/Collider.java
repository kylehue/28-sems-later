package colliders;

import javafx.scene.canvas.GraphicsContext;
import utils.GameUtils;
import utils.Vector;

import java.util.HashSet;

public abstract class Collider {
    private final String id = GameUtils.generateId();
    private final Vector position = new Vector();
    private final Vector velocity = new Vector();
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

    protected void update(float deltaTime, ColliderWorld world) {
        this.position.add(this.velocity);
        this.position.add(this.velocity.clone().scale(deltaTime));
        this.velocity.divide(world.getUpdateIterationCount());
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
