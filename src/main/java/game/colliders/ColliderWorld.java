package game.colliders;

import game.utils.Bounds;
import game.utils.Quadtree;

import java.util.ArrayList;
import java.util.HashSet;

public class ColliderWorld {
    private final ArrayList<Collider> colliders = new ArrayList<>();
    private Quadtree<Collider> quadtree = null;
    private Bounds bounds = new Bounds();
    
    public ColliderWorld() {
    
    }
    
    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }
    
    public Bounds getBounds() {
        return bounds;
    }
    
    public ArrayList<Collider> getColliders() {
        return colliders;
    }
    
    public void addCollider(Collider collider) {
        this.colliders.add(collider);
        
        if (collider instanceof GroupedCollider groupedCollider) {
            this.colliders.addAll(groupedCollider.getColliders());
        }
        
        collider.setColliderWorld(this);
    }
    
    public void removeCollider(String id) {
        for (int i = colliders.size() - 1; i >= 0; i--) {
            Collider collider = colliders.get(i);
            if (!collider.getId().equals(id)) continue;
            this.colliders.remove(i);
            collider.getContacts().remove(id);
            if (collider instanceof GroupedCollider groupedCollider) {
                this.colliders.removeAll(groupedCollider.getColliders());
            }
        }
    }
    
    public void removeCollider(Collider collider) {
        removeCollider(collider.getId());
    }
    
    public void setQuadtree(Quadtree<Collider> quadtree) {
        this.quadtree = quadtree;
    }
    
    public void fixedUpdate(float deltaTime) {
        for (Collider collider : colliders) {
            collider.getContacts().clear();
            collider.update(deltaTime);
        }
        
        HashSet<String> pairs = new HashSet<>();
        for (Collider colliderA : this.colliders) {
            // If there's a quadtree, use it
            ArrayList<Collider> otherColliders;
            if (this.quadtree != null) {
                otherColliders = colliderA.getAndUpdateNearColliders(quadtree);
            } else {
                otherColliders = this.colliders;
            }
            
            // Resolve
            for (Collider colliderB : otherColliders) {
                if (colliderA.getId().equals(colliderB.getId())) continue;
                if (colliderA.isAsleep() && colliderB.isAsleep()) {
                    continue;
                }
                
                /*
                 * Pair to avoid redundant collisions. For example, if we
                 * already checked objectA vs. objectB, we don't have to
                 * check objectB vs. objectA
                 */
                String combinationA = colliderA.getId() + colliderB.getId();
                String combinationB = colliderB.getId() + colliderA.getId();
                if (pairs.contains(combinationA) || pairs.contains(combinationB)) {
                    continue;
                }
                pairs.add(combinationA);
                pairs.add(combinationB);
                colliderA.resolveCollision(colliderB);
            }
        }
    }
}
