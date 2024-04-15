package game.inventory.weapons;

import game.utils.Vector;

public abstract class Gun extends Weapon {
    protected Vector muzzlePosition = new Vector();
    protected int fireRateInMillis = 1000;
    
    public Gun(String imageUrl) {
        super(imageUrl);
    }
    
    public Vector getMuzzlePosition() {
        return muzzlePosition;
    }
    
    public void setFireRateInMillis(int fireRateInMillis) {
        this.fireRateInMillis = fireRateInMillis;
    }
    
    public int getFireRateInMillis() {
        return fireRateInMillis;
    }
}
