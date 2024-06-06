package game.colliders;

import game.utils.*;
import game.utils.Common;
import javafx.scene.canvas.GraphicsContext;
import utils.*;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class Collider implements HashGrid.BoundedObject {
    public static final float VELOCITY_LIMIT = 8.0f;
    
    /* Misc */
    private ColliderWorld colliderWorld = null;
    private final int id = Common.generateId();
    
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
    private final HashSet<Integer> contacts = new HashSet<>();
    private ArrayList<Collider> nearColliders = new ArrayList<>();
    
    /* Grouping */
    private int category = 0xffff;
    private int mask = 0xffff;
    private int skipResolutionMask = 0;
    
    public void updateNearColliders(HashGrid<Collider> hashGrid) {
        Async.queue2.submit(() -> {
            updateNearCollidersImmediate(hashGrid);
        });
    }
    
    public void updateNearCollidersImmediate(HashGrid<Collider> hashGrid) {
        nearColliders = hashGrid.retrieve(
            this
        );
    }
    
    public ArrayList<Collider> getAndUpdateNearColliders(
        HashGrid<Collider> hashGrid
    ) {
        updateNearColliders(hashGrid);
        return nearColliders;
    }
    
    public ArrayList<Collider> getAndUpdateNearCollidersImmediate(
        HashGrid<Collider> hashGrid
    ) {
        updateNearCollidersImmediate(hashGrid);
        return nearColliders;
    }
    
    public ArrayList<Collider> getNearColliders() {
        return nearColliders;
    }
    
    /* Grouping */
    public void setCategory(int category) {
        this.category = category;
    }
    
    public void setMask(int mask) {
        this.mask = mask;
    }
    
    public void setSkipResolutionMask(int skipResolutionMask) {
        this.skipResolutionMask = skipResolutionMask;
    }
    
    public boolean shouldSkipResolutionWith(Collider collider) {
        return (collider.category & this.skipResolutionMask) != 0 || (collider.skipResolutionMask & this.category) != 0;
    }
    
    public boolean shouldCollideWith(Collider collider) {
        return (collider.category & this.mask) != 0 || (collider.mask & this.category) != 0;
    }
    
    public int getCategory() {
        return category;
    }
    
    public int getMask() {
        return mask;
    }
    
    public int getSkipResolutionMask() {
        return skipResolutionMask;
    }
    
    /* Contacts / collisions */
    public boolean isCollidingInBounds() {
        return isCollidingInBounds;
    }
    
    protected HashSet<Integer> getContacts() {
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
        return CollisionResolvers.getLineToColliderIntersectionPoint(pointA, pointB, this) != null;
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
        Vector target = new Vector(
            (velocityX + accelerationX * deltaTimeSquared) - friction * velocityX,
            (velocityY + accelerationY * deltaTimeSquared) - friction * velocityY
        );
        target.limit(VELOCITY_LIMIT);
        position.add(target);
        
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
    
    public int getId() {
        return id;
    }
    
    protected void copyAttributesToCollider(Collider collider) {
        collider.isStatic = isStatic;
        collider.mask = mask;
        collider.category = category;
        collider.skipResolutionMask = skipResolutionMask;
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
