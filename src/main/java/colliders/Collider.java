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
    private final HashSet<String> groups = new HashSet<>();
    private String groupId = "";
    
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
        if (
            groups.isEmpty() ||
                collider.groups.isEmpty() ||
                this.groupId.isEmpty() ||
                collider.groupId.isEmpty()
        ) {
            return true;
        }
        
        return groups.contains(collider.groupId);
    }
    
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
    
    protected void copyAttributesToCollider(Collider collider) {
        collider.groupId = groupId;
        collider.mass = mass;
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
