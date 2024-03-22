package entity;

import javafx.scene.canvas.GraphicsContext;
import utils.Vector;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public abstract class Entity {
    private final HashMap<String, Interval> registeredIntervals = new HashMap<>();
    private final Vector position = new Vector();
    private int currentHealth = 100;
    private int maxHealth = 100;
    
    public Vector getPosition() {
        return position;
    }
    
    public int getMaxHealth() {
        return maxHealth;
    }
    
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }
    
    public int getCurrentHealth() {
        return currentHealth;
    }
    
    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }
    
    private static class Interval {
        public int interval = 0;
        public long cooldown = 0;
    }
    
    public boolean isIntervalOverFor(String name) {
        long timeNow = System.nanoTime();
        Interval intervalAndCooldown = registeredIntervals.get(name);
        int interval = intervalAndCooldown.interval;
        long cooldown = intervalAndCooldown.cooldown;
        if (timeNow - cooldown > TimeUnit.MILLISECONDS.toNanos(interval)) {
            intervalAndCooldown.cooldown = timeNow;
            return true;
        }
        return false;
    }
    
    public void registerIntervalFor(String name, int intervalRateInMillis){
        Interval interval = new Interval();
        interval.interval = intervalRateInMillis;
        registeredIntervals.put(name, interval);
    }
    
    
    // to be overridden
    public void render(GraphicsContext ctx) {
    
    }
    
    // to be overridden
    public void update(float deltaTime) {
    
    }
}
