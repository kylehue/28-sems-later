package game.inventory.weapons;

import game.World;
import game.utils.Vector;

public abstract class Gun extends Weapon {
    protected Vector muzzlePosition = new Vector();
    protected int fireRateInMillis = 1000;
    private long lastShootTime = 0;
    
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
    
    protected abstract void handleShoot(
        World world,
        Vector initialPosition,
        float angle
    );
    
    public void shoot(World world, Vector initialPosition, float angle) {
        long timeNow = System.currentTimeMillis();
        if (timeNow - lastShootTime > fireRateInMillis) {
            float xOffset = muzzlePosition.getX() - handlePosition.getX() + 4;
            Vector computedInitialPosition = initialPosition.clone().add(
                (float) (Math.cos(angle) * xOffset),
                (float) (Math.sin(angle) * xOffset)
            );
            
            handleShoot(world, computedInitialPosition, angle);
            lastShootTime = timeNow;
        }
    }
}
