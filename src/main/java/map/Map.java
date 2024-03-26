package map;

import colliders.Collider;
import colliders.ColliderWorld;
import colliders.PolygonCollider;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import main.CollisionGroup;
import utils.Bounds;
import utils.Quadtree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public abstract class Map {
    public final ArrayList<Layer> layers = new ArrayList<>();
    protected final int tileSize;
    
    public Map(int tileSize) {
        this.tileSize = tileSize;
    }
    
    public Layer addLayer() {
        Layer layer = new Layer(this);
        layers.add(layer);
        return layer;
    }
    
    public ArrayList<Layer> getLayers() {
        return layers;
    }
    
    public int getTileSize() {
        return tileSize;
    }
    
    public int getTotalWidth() {
        return 32 * 4;
    }
    
    public int getTotalHeight() {
        return 32 * 4;
    }
}
