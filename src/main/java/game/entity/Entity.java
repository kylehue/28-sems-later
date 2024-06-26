package game.entity;

import game.Drawable;
import game.World;
import game.colliders.Collider;
import game.utils.Bounds;
import game.utils.IntervalMap;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.scene.canvas.GraphicsContext;
import game.utils.Common;
import game.utils.Vector;

public abstract class Entity implements Drawable {
    protected final int id = Common.generateId();
    protected final Vector position = new Vector();
    private final FloatProperty currentHealth = new SimpleFloatProperty(100);
    private final FloatProperty maxHealth = new SimpleFloatProperty(Float.MAX_VALUE);
    protected int zIndex = 0;
    
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
    
    public abstract void dispose();
    
    public void setMaxHealth(float maxHealth) {
        if (getCurrentHealth() > maxHealth) {
            setCurrentHealth(maxHealth);
        }
        
        this.maxHealth.set(maxHealth);
    }
    
    public void setCurrentHealth(float currentHealth) {
        this.currentHealth.set(
            Common.clamp(
                currentHealth,
                0,
                getMaxHealth()
            )
        );
    }
    
    public void addHealth(float health) {
        setCurrentHealth(getCurrentHealth() + health);
    }
    
    public float getCurrentHealth() {
        return currentHealth.get();
    }
    
    public FloatProperty currentHealthProperty() {
        return currentHealth;
    }
    
    public float getMaxHealth() {
        return maxHealth.get();
    }
    
    public FloatProperty maxHealthProperty() {
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
    
    public int getId() {
        return id;
    }
    
    public void addMaxHealth(float maxHealth){
        setMaxHealth(getMaxHealth() + maxHealth);
    }
    
    public abstract Collider getCollider();
    
    public Bounds getHitBox() {
        return new Bounds(
            position.getX() - 25,
            position.getY() - 25,
            50,
            50
        );
    }
}
