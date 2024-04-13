package colliders;

import utils.Bounds;
import utils.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GroupedCollider extends Collider {
    private final ArrayList<Collider> colliders = new ArrayList<>();
    private final HashMap<String, Vector> constraints = new HashMap<>();
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
            collider.setGroup(getGroup());
            collider.getGroups().addAll(getGroups());
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
        clone.setGroup(getGroup());
        clone.getGroups().addAll(getGroups());
        
        return clone;
    }
}
