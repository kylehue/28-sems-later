package game.colliders;

import game.utils.Bounds;
import game.utils.Quadtree;

import java.util.ArrayList;
import java.util.HashSet;

public class ColliderWorld {
    private final ArrayList<Collider> colliders = new ArrayList<>();
    private final Quadtree<Collider> quadtree;
    private Bounds bounds = new Bounds();
    
    public ColliderWorld(Quadtree<Collider> quadtree) {
        this.quadtree = quadtree;
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
    
    public void removeCollider(String id) {
        for (int i = colliders.size() - 1; i >= 0; i--) {
            Collider collider = colliders.get(i);
            if (!collider.getId().equals(id)) continue;
            this.colliders.remove(i);
            collider.getContacts().remove(id);
        }
    }
    
    public void removeCollider(Collider collider) {
        removeCollider(collider.getId());
    }
    
    private void addColliderToQuadtree(Collider collider) {
        quadtree.insert(
            collider,
            new Bounds(
                collider.getPosition().getX() - collider.getWidth() / 2f,
                collider.getPosition().getY() - collider.getHeight() / 2f,
                collider.getWidth(),
                collider.getHeight()
            )
        );
    }
    
    public void fixedUpdate(float deltaTime) {
        for (Collider collider : colliders) {
            collider.getContacts().clear();
            collider.update(deltaTime);
            addColliderToQuadtree(collider);
        }
        
        HashSet<String> pairs = new HashSet<>();
        for (Collider colliderA : this.colliders) {
            ArrayList<Collider> otherColliders = colliderA.getAndUpdateNearColliders(quadtree);
            
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
