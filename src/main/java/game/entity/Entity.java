package game.entity;

import game.World;
import javafx.scene.canvas.GraphicsContext;
import game.utils.Common;
import game.utils.Vector;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public abstract class Entity implements Drawable {
    protected final World world;
    private final HashMap<String, Interval> registeredIntervals = new HashMap<>();
    private final Vector position = new Vector();
    private int currentHealth = 100;
    private int maxHealth = 100;
    private int zIndex = 0;
    
    public Entity(World world) {
        this.world = world;
    }
    
    private static class Interval {
        public int interval = 0;
        public long coolDown = 0;
    }
    
    public boolean isIntervalOverFor(String name) {
        long timeNow = System.nanoTime();
        Interval intervalAndCooldown = registeredIntervals.get(name);
        if (intervalAndCooldown == null) return false;
        int interval = intervalAndCooldown.interval;
        long cooldown = intervalAndCooldown.coolDown;
        if (timeNow - cooldown > TimeUnit.MILLISECONDS.toNanos(interval)) {
            return true;
        }
        return false;
    }
    
    public void resetIntervalFor(String name) {
        Interval intervalAndCooldown = registeredIntervals.get(name);
        if (intervalAndCooldown == null) return;
        intervalAndCooldown.coolDown = System.nanoTime();
    }
    
    public void registerIntervalFor(String name, int intervalRateInMillis) {
        Interval interval = new Interval();
        interval.interval = intervalRateInMillis;
        registeredIntervals.put(name, interval);
    }
    
    public void changeIntervalFor(String name, int newIntervalInMillis) {
        registeredIntervals.get(name).interval = newIntervalInMillis;
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
    
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }
    
    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = (int) Common.clamp(
            currentHealth,
            0,
            this.maxHealth
        );
    }
    
    public int getCurrentHealth() {
        return currentHealth;
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
    
    public int getMaxHealth() {
        return maxHealth;
    }
}
