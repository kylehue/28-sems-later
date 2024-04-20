package game.inventory.weapons;

import game.World;
import game.projectiles.Bullet;
import game.utils.Vector;

public class Rifle extends Gun {
    private float bulletSpeed = 15000;
    private float accuracy = 0f; // 0-1 value
    private float maxDistance = 200;
    
    public Rifle() {
        super("/weapons/rifle.png");
        setFireRateInMillis(50);
        muzzlePosition.set(32, 4);
        handlePosition.set(13, 7);
    }
    
    public void setBulletSpeed(float bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }
    
    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }
    
    public void setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
    }
    
    public float getBulletSpeed() {
        return bulletSpeed;
    }
    
    public float getAccuracy() {
        return accuracy;
    }
    
    public float getMaxDistance() {
        return maxDistance;
    }
    
    @Override
    public void handleShoot(World world, Vector initialPosition, float angle) {
        // Add inaccuracy
        float maxInaccuracy = (float) (Math.PI / 8);
        angle += (float) (maxInaccuracy * (1 - accuracy) * Math.random() - maxInaccuracy / 2) * (1 - accuracy);
        
        Bullet bullet = world.spawnBullet(initialPosition, angle);
        bullet.setSpeed(bulletSpeed);
        bullet.setMaxDistance(maxDistance);
        bullet.setDamage(damage);
    }
}
