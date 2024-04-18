package game.colliders;

import game.utils.Bounds;
import game.utils.Common;
import game.utils.Quadtree;
import game.utils.Vector;
import javafx.scene.canvas.GraphicsContext;
import utils.*;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class Collider {
    /* Misc */
    private ColliderWorld colliderWorld = null;
    private final String id = Common.generateId();
    
    /* Physics */
    private final Vector position = new Vector();
    private final Vector oldPosition = new Vector();
    private final Vector velocity = new Vector();
    private final Vector acceleration = new Vector();
    private float friction = 0.1f;
    private float mass = 1.0f;
    private boolean isStatic = false;
    
    /* Collision */
    private boolean isCollidingInBounds = false;
    private final HashSet<String> contacts = new HashSet<>();
    private ArrayList<Collider> nearColliders = new ArrayList<>();
    
    /* Grouping */
    private final HashSet<Object> groups = new HashSet<>();
    private final HashSet<Object> excludedResolutions = new HashSet<>();
    private Object groupId = null;
    
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
    
    public ArrayList<Collider> getNearColliders() {
        return nearColliders;
    }
    
    /* Grouping */
    public void addToGroup(Object groupKey) {
        groups.add(groupKey);
    }
    
    public void removeFromGroup(Object groupKey) {
        groups.remove(groupKey);
    }
    
    protected HashSet<Object> getGroups() {
        return groups;
    }
    
    public void setGroup(Object groupKey) {
        this.groupId = groupKey;
    }
    
    protected Object getGroup() {
        return groupId;
    }
    
    public boolean isGroupedWith(Collider collider) {
        return groups.contains(collider.groupId);
    }
    
    public void excludeResolutionToGroup(Object groupId) {
        excludedResolutions.add(groupId);
    }
    
    public boolean isResolutionExcludedFromGroup(Object groupId) {
        return excludedResolutions.contains(groupId);
    }
    
    /* Contacts / collisions */
    public boolean isCollidingInBounds() {
        return isCollidingInBounds;
    }
    
    protected HashSet<String> getContacts() {
        return contacts;
    }
    
    /**
     * Check if the given collider is colliding with this collider.
     */
    public boolean isCollidingWith(Collider collider) {
        return contacts.contains(collider.getId());
    }
    
    /**
     * Check if the given bounds is colliding with this collider.
     */
    public boolean isCollidingWith(Bounds bounds) {
        return CollisionResolvers.testAABB(
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
    
    /**
     * Check if the given line is colliding with this collider.
     */
    public boolean isCollidingWith(Vector pointA, Vector pointB) {
        return CollisionResolvers.testLineToCollider(pointA, pointB, this);
    }
    
    public boolean isColliding() {
        return !contacts.isEmpty();
    }
    
    public boolean isAsleep() {
        if (acceleration.getX() != 0 || acceleration.getY() != 0) return false;
        return isStatic || velocity.getMagnitude() <= 0.001;
    }
    
    /* Physics / movement */
    public void setFriction(float friction) {
        this.friction = Common.clamp(friction, 0, 1);
    }
    
    public float getFriction() {
        return friction;
    }
    
    public void setMass(float mass) {
        this.mass = Common.clamp(mass, 1, Float.MAX_VALUE);
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
        float targetX = (velocityX + accelerationX * deltaTimeSquared) - friction * velocityX;
        float targetY = (velocityY + accelerationY * deltaTimeSquared) - friction * velocityY;
        position.add(
            targetX,
            targetY
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
        
        isCollidingInBounds = false;
        
        if (overlapLeft > 0) {
            position.addX(overlapLeft);
            isCollidingInBounds = true;
        }
        
        if (overlapRight > 0) {
            position.subtractX(overlapRight);
            isCollidingInBounds = true;
        }
        
        if (overlapTop > 0) {
            position.addY(overlapTop);
            isCollidingInBounds = true;
        }
        
        if (overlapBottom > 0) {
            position.subtractY(overlapBottom);
            isCollidingInBounds = true;
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
