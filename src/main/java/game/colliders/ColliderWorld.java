package game.colliders;

import game.utils.Bounds;
import game.utils.HashGrid;
import game.utils.Quadtree;

import java.util.ArrayList;
import java.util.HashSet;

public class ColliderWorld {
    private final ArrayList<Collider> colliders = new ArrayList<>();
    private final HashGrid<Collider> hashGrid;
    private Bounds bounds = new Bounds();
    
    public ColliderWorld(HashGrid<Collider> hashGrid) {
        this.hashGrid = hashGrid;
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
        collider.setColliderWorld(this);
    }
    
    public void removeCollider(int id) {
        for (int i = colliders.size() - 1; i >= 0; i--) {
            Collider collider = colliders.get(i);
            if (collider.getId() != id) continue;
            this.colliders.remove(i);
            collider.getContacts().remove(id);
        }
    }
    
    public void removeCollider(Collider collider) {
        removeCollider(collider.getId());
    }
    
    private void addColliderToQuadtree(Collider collider) {
        hashGrid.insert(collider);
    }
    
    private String getPairHash(Collider colliderA, Collider colliderB) {
        return colliderA.getId() > colliderB.getId() ?
            colliderA.getId() + ":" + colliderB.getId() :
            colliderB.getId() + ":" + colliderA.getId();
    }
    
    public void fixedUpdate(float deltaTime) {
        for (Collider collider : colliders) {
            collider.getContacts().clear();
            collider.update(deltaTime);
            addColliderToQuadtree(collider);
        }
        
        HashSet<String> pairs = new HashSet<>();
        for (Collider colliderA : this.colliders) {
            ArrayList<Collider> otherColliders = colliderA.getAndUpdateNearColliders(hashGrid);
            
            // Resolve
            for (Collider colliderB : otherColliders) {
                if (colliderB == null) continue;
                if (colliderA.getId() == colliderB.getId()) continue;
                if (colliderA.isAsleep() && colliderB.isAsleep()) {
                    continue;
                }
                
                if (!colliderA.shouldCollideWith(colliderB)) {
                    continue;
                }
                
                /*
                 * Pair to avoid redundant collisions. For example, if we
                 * already checked objectA vs. objectB, we don't have to
                 * check objectB vs. objectA
                 */
                String pairHash = getPairHash(colliderA, colliderB);
                if (pairs.contains(pairHash)) {
                    continue;
                }
                pairs.add(pairHash);
                colliderA.resolveCollision(colliderB);
            }
        }
    }
}
