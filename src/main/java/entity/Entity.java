package entity;

import utils.Vector;

public abstract class Entity {
    private final Vector position = new Vector();
    private final Vector velocity = new Vector();
    
    public Vector getPosition() {
        return position;
    }
    
    public Vector getVelocity() {
        return velocity;
    }
}
