package entity;

import javafx.scene.canvas.GraphicsContext;
import utils.Vector;

public abstract class Entity {
    private final Vector position = new Vector();
    private int currentHealth = 100;
    private int maxHealth = 100;
    
    public Vector getPosition() {
        return position;
    }
    
    public int getMaxHealth() {
        return maxHealth ;
    }
    
    public void setMaxHealth(int maxHealth){
        this.maxHealth = maxHealth;
    }
    
    public int getCurrentHealth() {
        return currentHealth;
    }
    
    public void setCurrentHealth(int currentHealth){
        this.currentHealth = currentHealth;
    }
    
    
    // to be overridden
    public void render(GraphicsContext ctx) {
    
    }
    
    // to be overridden
    public void update(float deltaTime) {
    
    }
}
