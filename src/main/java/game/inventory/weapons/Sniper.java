package game.inventory.weapons;

import game.World;
import game.projectiles.Bullet;
import game.projectiles.InstantBullet;
import game.utils.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sniper extends Gun {
    private float penetration = 15000;
    
    public Sniper() {
        super("/weapons/sniper.png");
        setFireRateInMillis(500);
        muzzlePosition.set(52, 7);
        handlePosition.set(24, 10);
    }
    
    public void setPenetration(float penetration) {
        this.penetration = penetration;
    }
    
    public float getPenetration() {
        return penetration;
    }
    
    @Override
    public void handleShoot(World world, Vector initialPosition, float angle) {
        InstantBullet bullet = world.spawnInstantBullet(initialPosition, angle);
        bullet.setPenetration(penetration);
        bullet.setDamage(damage);
    }
}
