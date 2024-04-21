package game.inventory.weapons;

import game.Config;
import game.World;
import game.projectiles.Bullet;
import game.utils.Vector;

public class Shotgun extends Gun {
    private float bulletSpeed = 0;
    private float maxDistance = 0;
    private float spreadInRadians = 0;
    
    public Shotgun() {
        super("/weapons/shotgun.png");
        setFireRateInMillis(Config.DEFAULT_SHOTGUN_FIRE_RATE_MILLIS);
        setBulletSpeed(Config.DEFAULT_SHOTGUN_BULLET_SPEED);
        setMaxDistance(Config.DEFAULT_SHOTGUN_BULLET_MAX_DISTANCE);
        setSpreadInRadians(Config.DEFAULT_SHOTGUN_SPREAD_RADIANS);
        setDamage(Config.DEFAULT_SHOTGUN_BULLET_DAMAGE);
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
