package entity;

import utils.Vector;

public abstract class Entity {
    private double health = 0;
    private final Vector position = new Vector();
    private final Vector velocity = new Vector();
    
    public void setHealth(double health) {
        this.health = health;
    }
    
    public double getHealth() {
        return health;
    }
    
    public Vector getPosition() {
        return position;
    }
    
    public Vector getVelocity() {
        return velocity;
    }
}
