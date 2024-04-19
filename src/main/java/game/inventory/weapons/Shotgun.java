package game.inventory.weapons;

import game.World;
import game.projectiles.Bullet;
import game.utils.Vector;

public class Shotgun extends Gun {
    private float bulletSpeed = 15000;
    private float maxDistance = 200;
    private float spreadInRadians = (float) (Math.PI / 4);
    
    public Shotgun() {
        super("/weapons/shotgun.png");
        setFireRateInMillis(300);
        muzzlePosition.set(32, 4);
        handlePosition.set(14, 6);
    }
    
    public void setBulletSpeed(float bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }
    
    public void setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
    }
    
    public void setSpreadInRadians(float spreadInRadians) {
        this.spreadInRadians = spreadInRadians;
    }
    
    public float getBulletSpeed() {
        return bulletSpeed;
    }
    
    public float getMaxDistance() {
        return maxDistance;
    }
    
    public float getSpreadInRadians() {
        return spreadInRadians;
    }
    
    @Override
    public void handleShoot(World world, Vector initialPosition, float angle) {
        int bulletCount = (int) (spreadInRadians * 16);
        for (int i = 0; i < bulletCount; i++) {
            float angleBias = (float) (
                spreadInRadians * Math.random() - spreadInRadians / 2
            );
            float computedAngle = angle + angleBias;
            Bullet bullet = world.spawnBullet(initialPosition, computedAngle);
            bullet.setSpeed(bulletSpeed);
            bullet.setMaxDistance(maxDistance);
            bullet.setDamage(damage);
        }
    }
}
