package colliders;

import utils.Bounds;
import utils.Quadtree;

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
        collider.setColliderWorld(this);
    }
    
    public void removeCollider(String id) {
        for (int i = colliders.size() - 1; i >= 0; i--) {
            if (colliders.get(i).getId().equals(id)) {
                this.colliders.remove(i);
                break;
            }
        }
    }
    
    public void removeCollider(Collider collider) {
        this.colliders.remove(collider);
    }
    
    public void setQuadtree(Quadtree<Collider> quadtree) {
        this.quadtree = quadtree;
    }
    
    public void update(float deltaTime) {
        HashSet<String> pairs = new HashSet<>();
        for (Collider colliderA : this.colliders) {
            colliderA.update(deltaTime);
            
            // If there's a quadtree, use it
            ArrayList<Collider> otherColliders;
            if (this.quadtree != null) {
                float width = colliderA.getWidth();
                float height = colliderA.getHeight();
                otherColliders = new ArrayList<>(
                    this.quadtree.retrieve(
                        colliderA.getPosition().getX() - width / 2,
                        colliderA.getPosition().getY() - height / 2,
                        width,
                        height
                    ).stream().map(e -> e.object).toList()
                );
            } else {
                otherColliders = this.colliders;
            }
            
            // Resolve
            for (Collider colliderB : otherColliders) {
                if (colliderA.getId().equals(colliderB.getId())) continue;
                
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
