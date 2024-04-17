package game.entity;

import game.Drawable;
import game.World;
import game.colliders.Collider;
import game.utils.IntervalMap;
import javafx.scene.canvas.GraphicsContext;
import game.utils.Common;
import game.utils.Vector;

public abstract class Entity extends IntervalMap implements Drawable {
    protected final String id = Common.generateId();
    protected final World world;
    protected final Vector position = new Vector();
    protected float currentHealth = 100;
    protected float maxHealth = 100;
    protected int zIndex = 0;
    
    public Entity(World world) {
        this.world = world;
    }
    
    // to be overridden
    @Override
    public Vector getRenderPosition() {
        return this.position;
    }
    
    // to be overridden
    @Override
    public abstract void render(GraphicsContext ctx, float alpha);
    
    // to be overridden
    public void fixedUpdate(float deltaTime) {
    }
    
    // to be overridden
    public void update(float deltaTime) {
    }
    
    @Override
    public boolean isSeeThrough() {
        return false;
    }
    
    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }
    
    public void setCurrentHealth(float currentHealth) {
        this.currentHealth = Common.clamp(
            currentHealth,
            0,
            this.maxHealth
        );
    }
    
    public void addHealth(float health){
       this.currentHealth += health;
    }
    
    public float getCurrentHealth() {
        return currentHealth;
    }
    
    public float getMaxHealth() {
        return maxHealth;
    }
    
    public int getZIndex() {
        return this.zIndex;
    }
    
    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }
    
    public Vector getPosition() {
        return position;
    }
    
    public String getId() {
        return id;
    }
    
    public abstract Collider getCollider();
}
