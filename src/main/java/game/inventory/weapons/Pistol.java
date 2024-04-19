package game.inventory.weapons;

import game.World;
import game.projectiles.Bullet;
import game.utils.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Pistol extends Gun {
    private float bulletSpeed = 15000;
    private float maxDistance = 200;
    
    public Pistol() {
        super("/weapons/pistol.png");
        setFireRateInMillis(250);
        muzzlePosition.set(14, 3);
        handlePosition.set(3, 6);
    }
    
    public void setBulletSpeed(float bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }
    
    public void setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
    }
    
    public float getBulletSpeed() {
        return bulletSpeed;
    }
    
    public float getMaxDistance() {
        return maxDistance;
    }
    
    @Override
    public void handleShoot(World world, Vector initialPosition, float angle) {
        Bullet bullet = world.spawnBullet(initialPosition, angle);
        bullet.setSpeed(bulletSpeed);
        bullet.setMaxDistance(maxDistance);
        bullet.setDamage(damage);
    }
}
