package colliders;

import javafx.scene.canvas.GraphicsContext;
import utils.*;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class Collider {
    private ColliderWorld colliderWorld = null;
    private final String id = GameUtils.generateId();
    private final Vector position = new Vector();
    private final Vector oldPosition = new Vector();
    private final Vector acceleration = new Vector();
    private float friction = 0.1f;
    private float mass = 1.0f;
    private final Vector velocity = new Vector();
    private boolean isStatic = false;
    private final HashSet<String> contacts = new HashSet<>();
    private final HashSet<String> groups = new HashSet<>();
    private final HashSet<String> excludedResolutions = new HashSet<>();
    private String groupId = "";
    private ArrayList<Collider> nearColliders = new ArrayList<>();
    
    public ArrayList<Collider> getAndUpdateNearColliders(Quadtree<Collider> quadtree) {
        Async.queue2.submit(() -> {
            float width = getWidth();
            float height = getHeight();
            nearColliders = new ArrayList<>(
                quadtree.retrieve(
                    getPosition().getX() - width / 2,
                    getPosition().getY() - height / 2,
                    width,
                    height
                ).stream().map(e -> e.object).toList()
            );
        });
        
        return nearColliders;
    }
    
    /* Grouping */
    public void addToGroup(String groupId) {
        groups.add(groupId);
    }
    
    public void removeFromGroup(String groupId) {
        groups.remove(groupId);
    }
    
    protected HashSet<String> getGroups() {
        return groups;
    }
    
    public void setGroup(String groupId) {
        this.groupId = groupId;
    }
    
    protected String getGroup() {
        return groupId;
    }
    
    public boolean isAsleep() {
        if (acceleration.getX() != 0 || acceleration.getY() != 0) return false;
        return isStatic || velocity.getMagnitude() <= 0.001;
    }
    
    public boolean isGroupedWith(Collider collider) {
        return groups.contains(collider.groupId);
    }
    
    public void excludeResolutionToGroup(String groupId) {
        excludedResolutions.add(groupId);
    }
    
    public boolean isResolutionExcludedFromGroup(String groupId) {
        return excludedResolutions.contains(groupId);
    }
    
    /* Contacts / collisions */
    protected HashSet<String> getContacts() {
        return contacts;
    }
    
    public boolean isCollidingWith(Collider collider) {
        return contacts.contains(collider.getId());
    }
    
    public boolean isCollidingWith(Bounds bounds) {
        return CollisionResolvers.AABBTest(
            position.getX() - getWidth() / 2,
            position.getY() - getHeight() / 2,
            getWidth(),
            getHeight(),
            bounds.getX(),
            bounds.getY(),
            bounds.getWidth(),
            bounds.getHeight()
        );
    }
    
    public boolean isColliding() {
        return !contacts.isEmpty();
    }
    
    /* Physics / movement */
    public void setFriction(float friction) {
        this.friction = GameUtils.clamp(friction, 0, 1);
    }
    
    public float getFriction() {
        return friction;
    }
    
    public void setMass(float mass) {
        this.mass = GameUtils.clamp(mass, 1, Float.MAX_VALUE);
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
    
    public void setPosition(float x, float y) {
        position.set(x, y);
        oldPosition.set(x, y);
        velocity.set(0, 0);
    }
    
    public void setPosition(Vector pos) {
        setPosition(pos.getX(), pos.getY());
    }
    
    public Vector getVelocity() {
        return velocity;
    }
    
    public void applyForceX(float x) {
        this.acceleration.addX(x / mass);
    }
    
    public void applyForceY(float y) {
        this.acceleration.addY(y / mass);
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
        assert colliderWorld != null;
        
        if (!ticked) {
            oldPosition.set(position);
            ticked = true;
            return;
        }
        
        velocity.set(position.clone().subtract(oldPosition));
        Vector temp = position.clone();
        
        // Update current position
        float velocityX = velocity.getX();
        float velocityY = velocity.getY();
        float accelerationX = acceleration.getX();
        float accelerationY = acceleration.getY();
        float deltaTimeSquared = deltaTime * deltaTime;
        position.add(
            (velocityX + accelerationX * deltaTimeSquared) - friction * velocityX,
            (velocityY + accelerationY * deltaTimeSquared) - friction * velocityY
        );
        
        this.subUpdate(deltaTime);
        
        this.limitPositionToWorldBounds();
        
        // Update old position
        oldPosition.set(temp);
        
        acceleration.set(0, 0);
    }
    
    private void limitPositionToWorldBounds() {
        // Limit position to world bounds
        float boundsWidth = colliderWorld.getBounds().getWidth();
        float boundsHeight = colliderWorld.getBounds().getHeight();
        float boundsLeft = colliderWorld.getBounds().getX();
        float boundsRight = boundsLeft + boundsWidth;
        float boundsTop = colliderWorld.getBounds().getY();
        float boundsBottom = boundsTop + boundsHeight;
        
        float overlapLeft = Math.max(0, boundsLeft - position.getX() + getWidth() / 2);
        float overlapRight = Math.max(0, position.getX() + getWidth() / 2 - boundsRight);
        float overlapTop = Math.max(0, boundsTop - position.getY() + getHeight() / 2);
        float overlapBottom = Math.max(0, position.getY() + getHeight() / 2 - boundsBottom);
        
        if (overlapLeft > 0) {
            position.addX(overlapLeft);
        }
        
        if (overlapRight > 0) {
            position.subtractX(overlapRight);
        }
        
        if (overlapTop > 0) {
            position.addY(overlapTop);
        }
        
        if (overlapBottom > 0) {
            position.subtractY(overlapBottom);
        }
    }
    
    /* Misc */
    protected void setColliderWorld(ColliderWorld colliderWorld) {
        this.colliderWorld = colliderWorld;
    }
    
    public ColliderWorld getColliderWorld() {
        return this.colliderWorld;
    }
    
    public String getId() {
        return id;
    }
    
    protected void copyAttributesToCollider(Collider collider) {
        collider.groupId = groupId;
        collider.isStatic = isStatic;
        collider.groups.addAll(groups);
        collider.groupId = groupId;
        collider.mass = mass;
        collider.friction = friction;
        collider.position.set(this.position);
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
    
    // to be overridden
    public void subUpdate(float deltaTime) {
    
    }
}
