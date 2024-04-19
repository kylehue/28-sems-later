package game.inventory.weapons;

import game.World;
import game.projectiles.Bullet;
import game.projectiles.Grenade;
import game.utils.Vector;

public class GrenadeLauncher extends Gun {
    private float aoeDistance = 60;
    
    public GrenadeLauncher() {
        super("/weapons/grenade-launcher.png");
        setFireRateInMillis(500);
        muzzlePosition.set(41, 4);
        handlePosition.set(24, 7);
    }
    
    public void setAoeDistance(float aoeDistance) {
        this.aoeDistance = aoeDistance;
    }
    
    public float getAoeDistance() {
        return aoeDistance;
    }
    
    @Override
    public void handleShoot(World world, Vector initialPosition, float angle) {
        Grenade grenade = world.spawnGrenade(initialPosition, angle);
        grenade.setDamage(damage);
        grenade.setAoeDistance(aoeDistance);
    }
}
