package game.map;

import game.colliders.Collider;
import game.colliders.ColliderWorld;
import game.colliders.GroupedCollider;
import game.utils.Bounds;
import game.utils.Quadtree;

import java.util.ArrayList;

public abstract class Map {
    protected final ArrayList<Layer> layers = new ArrayList<>();
    protected final int tileSize;
    private int totalWidth = 0;
    private int totalHeight = 0;
    
    public Map(int tileSize) {
        this.tileSize = tileSize;
    }
    
    public Layer addLayer() {
        Layer layer = new Layer(this);
        layers.add(layer);
        this.updateTotalSize();
        return layer;
    }
    
    public ArrayList<Layer> getLayers() {
        return layers;
    }
    
    public int getTileSize() {
        return tileSize;
    }
    
    protected void updateTotalSize() {
        int maxWidth = 0;
        int maxHeight = 0;
        for (Layer layer : layers) {
            maxWidth = Math.max(maxWidth, layer.getTotalWidth());
            maxHeight = Math.max(maxHeight, layer.getTotalHeight());
        }
        
        this.totalWidth = maxWidth;
        this.totalHeight = maxHeight;
    }
    
    public int getTotalWidth() {
        return this.totalWidth;
    }
    
    public int getTotalHeight() {
        return this.totalHeight;
    }
    
    public void fixedUpdate(float deltaTime) {
        for (Layer layer : layers) {
            layer.fixedUpdate(deltaTime);
        }
    }
    
    public void addCollidersToWorld(ColliderWorld colliderWorld) {
        for (Layer layer : layers) {
            for (Material material : layer.getMaterials()) {
                Collider collider = material.getCollider();
                if (collider == null) continue;
                colliderWorld.addCollider(collider);
            }
        }
    }
}
