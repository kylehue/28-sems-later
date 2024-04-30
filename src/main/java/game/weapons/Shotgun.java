package game.weapons;

import game.Config;
import game.Game;
import game.World;
import game.projectiles.Bullet;
import game.utils.Vector;

public class Shotgun extends Gun {
    private float bulletSpeed = 0;
    private float maxDistance = 0;
    private float spreadInRadians = 0;
    private float penetration = 0;
    
    public Shotgun() {
        super("/weapons/shotgun.png");
        setMuzzlePosition(new Vector(32, 4));
        setOrigHandlePosition(new Vector(14, 6));
        resetStats();
    }
    
    public void setPenetration(float penetration) {
        this.penetration = penetration;
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
    
    public float getPenetration() {
        return penetration;
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
    public void resetStats() {
        setFireRateInMillis(Config.DEFAULT_SHOTGUN_FIRE_RATE_MILLIS);
        setBulletSpeed(Config.DEFAULT_SHOTGUN_BULLET_SPEED);
        setMaxDistance(Config.DEFAULT_SHOTGUN_BULLET_MAX_DISTANCE);
        setSpreadInRadians(Config.DEFAULT_SHOTGUN_SPREAD_RADIANS);
        setDamage(Config.DEFAULT_SHOTGUN_BULLET_DAMAGE);
        setPenetration(Config.DEFAULT_SHOTGUN_BULLET_PENETRATION);
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
            bullet.setDamage(damage);
            bullet.setPenetration(penetration);
            bullet.setSpeed(bulletSpeed);
            bullet.setMaxDistance(maxDistance);
        }
        
        Game.world.addPlayerDistanceAwareAudio(
            "/sounds/shotgun-shot.mp3",
            initialPosition,
            250
        );
    }
}
