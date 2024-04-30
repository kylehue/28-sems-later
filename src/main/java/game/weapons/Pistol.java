package game.weapons;

import game.Config;
import game.Game;
import game.World;
import game.projectiles.Bullet;
import game.utils.Vector;

public class Pistol extends Gun {
    private float bulletSpeed = 0;
    private float maxDistance = 0;
    private float penetration = 0;
    
    public Pistol() {
        super("/weapons/pistol.png");
        setMuzzlePosition(new Vector(14, 3));
        setOrigHandlePosition(new Vector(3, 6));
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
    
    public float getPenetration() {
        return penetration;
    }
    
    public float getBulletSpeed() {
        return bulletSpeed;
    }
    
    public float getMaxDistance() {
        return maxDistance;
    }
    
    @Override
    public void resetStats() {
        setFireRateInMillis(Config.DEFAULT_PISTOL_FIRE_RATE_MILLIS);
        setBulletSpeed(Config.DEFAULT_PISTOL_BULLET_SPEED);
        setMaxDistance(Config.DEFAULT_PISTOL_BULLET_MAX_DISTANCE);
        setDamage(Config.DEFAULT_PISTOL_BULLET_DAMAGE);
        setPenetration(Config.DEFAULT_PISTOL_BULLET_PENETRATION);
    }
    
    @Override
    public void handleShoot(World world, Vector initialPosition, float angle) {
        Bullet bullet = world.spawnBullet(initialPosition, angle);
        bullet.setDamage(damage);
        bullet.setPenetration(penetration);
        bullet.setSpeed(bulletSpeed);
        bullet.setMaxDistance(maxDistance);
    }
    
    @Override
    protected void handleSound(Vector initialPosition) {
        Game.world.addPlayerDistanceAwareAudio(
            "/sounds/gun-shot.mp3",
            initialPosition,
            250
        );
    }
}
