package game.entity;

import game.World;
import game.utils.IntervalMap;
import javafx.scene.canvas.GraphicsContext;
import game.utils.Common;
import game.utils.Vector;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public abstract class Entity extends IntervalMap implements Drawable {
    protected final World world;
    private final Vector position = new Vector();
    private float currentHealth = 100;
    private float maxHealth = 100;
    private int zIndex = 0;
    
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
}
