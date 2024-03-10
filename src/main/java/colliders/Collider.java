package colliders;

import javafx.scene.canvas.GraphicsContext;
import utils.GameUtils;
import utils.Vector;

import java.util.HashSet;

public abstract class Collider {
    private final String id = GameUtils.generateId();
    private final Vector position = new Vector();
    private final Vector oldPosition = new Vector();
    private final Vector acceleration = new Vector();
    private float friction = 0.05f;
    private float mass = 1.0f;
    private final Vector velocity = new Vector();
    private boolean isStatic = false;
    private final HashSet<String> contacts = new HashSet<>();
    private final HashSet<String> groups = new HashSet<>();
    private String groupId = "";
    
    /* Grouping */
    public void addToGroup(String groupId) {
        groups.add(groupId);
    }
    
    public void removeFromGroup(String groupId) {
        groups.remove(groupId);
    }
    
    public void setGroup(String groupId) {
        this.groupId = groupId;
    }
    
    protected String getGroup() {
        return groupId;
    }
    
    public boolean isGroupedWith(Collider collider) {
        return groups.contains(collider.groupId);
    }
    
    /* Contacts / collisions */
    protected HashSet<String> getContacts() {
        return contacts;
    }
    
    public boolean isCollidingWith(Collider collider) {
        return contacts.contains(collider.getId());
    }
    
    public boolean isColliding() {
        return !contacts.isEmpty();
    }
    
    /* Physics / movement */
    public void setFriction(float friction) {
        this.friction = friction;
    }
    
    public float getFriction() {
        return friction;
    }
    
    public void setMass(float mass) {
        this.mass = mass;
    }
    
    public float getMass() {
        return mass;
    }
    
    public Vector getAcceleration() {
        return acceleration;
    }
    
    public boolean isStatic() {
        return isStatic;
    }
    
    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
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
    
    public void applyForceX(float x) {
        this.acceleration.addX(x);
    }
    
    public void applyForceY(float y) {
        this.acceleration.addY(y);
    }
    
    public void applyForce(float x, float y) {
        this.applyForceX(x);
        this.applyForceY(y);
    }
    
    public void applyForce(Vector vector) {
        this.applyForce(vector.getX(), vector.getY());
    }
    
    boolean ticked = false;
    protected void update(float deltaTime) {
        if (!ticked) {
            oldPosition.set(position);
            ticked = true;
        }
        
        velocity.set(position.clone().subtract(oldPosition));
        oldPosition.set(position);
        
        position.add(
            velocity.clone().add(acceleration.clone().scale(deltaTime))
        );
        
        position.subtract(
            velocity.clone().scale(friction)
        );
        
        acceleration.set(0, 0);
    }
    
    /* Misc */
    public String getId() {
        return id;
    }
    
    protected void copyAttributesToCollider(Collider collider) {
        collider.groupId = groupId;
        collider.isStatic = isStatic;
        collider.groups.addAll(groups);
        collider.groupId = groupId;
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
    
    // to be overridden
    public Collider clone() {
        return this;
    }
}
