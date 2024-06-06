package game.colliders;

import game.utils.Bounds;
import game.utils.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GroupedCollider extends Collider {
    private final ArrayList<Collider> colliders = new ArrayList<>();
    private final HashMap<Integer, Vector> constraints = new HashMap<>();
    private float width = 0;
    private float height = 0;
    
    public GroupedCollider(Collider[] colliders) {
        this.colliders.addAll(Arrays.stream(colliders).toList());
        
        for (Collider collider : colliders) {
            Vector distance = collider.getPosition().clone().subtract(getPosition());
            constraints.put(collider.getId(), distance);
        }
        
        width = getBounds().getWidth();
        height = getBounds().getHeight();
    }
    
    private Bounds getBounds() {
        float minX = Float.MAX_VALUE;
        float maxX = -Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float maxY = -Float.MAX_VALUE;
        for (Collider collider : colliders) {
            minX = Math.min(minX, collider.getPosition().getX() - collider.getWidth() / 2);
            maxX = Math.max(maxX, collider.getPosition().getX() + collider.getWidth() / 2);
            minY = Math.min(minY, collider.getPosition().getY() - collider.getHeight() / 2);
            maxY = Math.max(maxY, collider.getPosition().getY() + collider.getHeight() / 2);
        }
        
        minX = minX == Float.MAX_VALUE ? 0 : minX;
        minY = minY == Float.MAX_VALUE ? 0 : minY;
        maxX = maxX == -Float.MAX_VALUE ? 0 : maxX;
        maxY = maxY == -Float.MAX_VALUE ? 0 : maxY;
        
        return new Bounds(
            (minX + maxX) / 2,
            (minY + maxY) / 2,
            maxX - minX,
            maxY - minY
        );
    }
    
    
    
    /**
     * Check if the given collider is colliding with this collider.
     */
    @Override
    public boolean isCollidingWith(Collider collider) {
        for (Collider _collider : colliders) {
            boolean isColliding = _collider.isCollidingWith(collider);
            if (isColliding) return true;
        }
        
        return false;
    }
    
    /**
     * Check if the given bounds is colliding with this collider.
     */
    @Override
    public boolean isCollidingWith(Bounds bounds) {
        for (Collider _collider : colliders) {
            boolean isColliding = _collider.isCollidingWith(bounds);
            if (isColliding) return true;
        }
        
        return false;
    }
    
    /**
     * Check if the given line is colliding with this collider.
     */
    @Override
    public boolean isCollidingWith(Vector pointA, Vector pointB) {
        for (Collider _collider : colliders) {
            boolean isColliding = _collider.isCollidingWith(pointA, pointB);
            if (isColliding) return true;
        }
        
        return false;
    }
    
    @Override
    public float getWidth() {
        return width;
    }
    
    @Override
    public float getHeight() {
        return height;
    }
    
    private void setup() {
        for (Collider collider : colliders) {
            collider.setPosition(collider.getPosition().clone().add(getPosition()));
            collider.setMass(getMass());
            collider.setFriction(getFriction());
            collider.setStatic(isStatic());
            collider.setMask(getMask());
            collider.setCategory(getCategory());
            collider.setColliderWorld(getColliderWorld());
        }
    }
    
    public ArrayList<Collider> getColliders() {
        return colliders;
    }
    
    private boolean ticked = false;
    
    @Override
    public void subUpdate(float deltaTime) {
        if (!ticked) {
            this.setup();
            ticked = true;
        }
        
        Bounds bounds = getBounds();
        getPosition().set(bounds.getX(), bounds.getY());
        
        // maintain the colliders distance from center
        for (Collider collider : colliders) {
            Vector targetDistance = constraints.get(collider.getId());
            collider.getPosition().set(getPosition().clone().add(targetDistance));
        }
    }
    
    @Override
    public Collider clone() {
        GroupedCollider clone = new GroupedCollider(
            this.colliders.stream().map(Collider::clone).toArray(Collider[]::new)
        );
        clone.setMass(getMass());
        clone.setFriction(getFriction());
        clone.setStatic(isStatic());
        clone.setMask(getMask());
        clone.setCategory(getCategory());
        
        return clone;
    }
    
    private void resolveCollisions(Collider colliderA, Collider colliderB) {
        if (
            colliderA instanceof PolygonCollider &&
                colliderB instanceof PolygonCollider
        ) {
            CollisionResolvers.polygonToPolygon(
                (PolygonCollider) colliderA,
                (PolygonCollider) colliderB
            );
        } else if (
            colliderA instanceof CircleCollider &&
                colliderB instanceof PolygonCollider
        ) {
            CollisionResolvers.circleToPolygon(
                (CircleCollider) colliderA,
                (PolygonCollider) colliderB
            );
        } else if (
            colliderB instanceof CircleCollider &&
                colliderA instanceof PolygonCollider
        ) {
            CollisionResolvers.circleToPolygon(
                (CircleCollider) colliderB,
                (PolygonCollider) colliderA
            );
        } else if (
            colliderA instanceof CircleCollider &&
                colliderB instanceof CircleCollider
        ) {
            CollisionResolvers.circleToCircle(
                (CircleCollider) colliderA,
                (CircleCollider) colliderB
            );
        }
    }
    
    @Override
    public void resolveCollision(Collider otherCollider) {
        getContacts().clear();
        for (Collider collider : colliders) {
            resolveCollisions(collider, otherCollider);
            getContacts().addAll(collider.getContacts());
        }
    }
}