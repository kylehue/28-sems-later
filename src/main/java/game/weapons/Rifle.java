package game.weapons;

import game.Config;
import game.World;
import game.projectiles.Bullet;
import game.utils.Vector;

public class Rifle extends Gun {
    private float bulletSpeed = 0;
    private float accuracy = 0; // 0-1 value
    private float maxDistance = 0;
    private float penetration = 0;
    
    public Rifle() {
        super("/weapons/rifle.png");
        setFireRateInMillis(Config.DEFAULT_RIFLE_FIRE_RATE_MILLIS);
        setAccuracy(Config.DEFAULT_RIFLE_ACCURACY);
        setBulletSpeed(Config.DEFAULT_RIFLE_BULLET_SPEED);
        setMaxDistance(Config.DEFAULT_RIFLE_BULLET_MAX_DISTANCE);
        setDamage(Config.DEFAULT_RIFLE_BULLET_DAMAGE);
        setPenetration(Config.DEFAULT_RIFLE_BULLET_PENETRATION);
        setMuzzlePosition(new Vector(32, 4));
        setOrigHandlePosition(new Vector(13, 7));
    }
    
    public void setPenetration(float penetration) {
        this.penetration = penetration;
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
    
    public float getPenetration() {
        return penetration;
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
