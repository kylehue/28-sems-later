package game.projectiles;

import game.Game;
import game.Drawable;
import game.World;
import game.entity.Entity;
import game.utils.Vector;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.scene.canvas.GraphicsContext;

import java.util.HashSet;

public abstract class Projectile implements Drawable {
    private final FloatProperty damage = new SimpleFloatProperty(100);
    protected final World world;
    protected float angle = 0;
    protected final Vector initialPosition = new Vector();
    protected final Vector position = new Vector();
    protected boolean isDisposed = false;
    protected HashSet<String> markedEntities = new HashSet<>();
    
    public Projectile(World world, Vector initialPosition, float angle) {
        this.world = world;
        this.initialPosition.set(initialPosition);
        this.position.set(initialPosition);
        this.angle = angle;
    }
    
    public void markEntity(Entity entity) {
        markedEntities.add(entity.getId());
    }
    
    public boolean isEntityMarked(Entity entity) {
        return markedEntities.contains(entity.getId());
    }
    
    public void setAngle(float angle) {
        this.angle = angle;
    }
    
    public void setDamage(float damage) {
        this.damage.set(damage);
    }
    
    public float getDamage() {
        return damage.get();
    }
    
    public FloatProperty damageProperty() {
        return damage;
    }
    
    public Vector getPosition() {
        return position;
    }
    
    public float getAngle() {
        return angle;
    }
    
    public Vector getInitialPosition() {
        return initialPosition;
    }
    
    public abstract void dispose();
    
    public boolean isDisposed() {
        return isDisposed;
    }
    
    public abstract void render(GraphicsContext graphicsContext, float alpha);
    
    public void fixedUpdate(float deltaTime) {
    
    }
    
    public void update(float deltaTime) {
    
    }
    
    @Override
    public boolean isSeeThrough() {
        return false;
    }
    
    @Override
    public int getZIndex() {
        return Game.ZIndex.PROJECTILE;
    }
    
    @Override
    public Vector getRenderPosition() {
        return position;
    }
}
