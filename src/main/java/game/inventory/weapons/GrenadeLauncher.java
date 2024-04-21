package game.inventory.weapons;

import game.Config;
import game.World;
import game.projectiles.Bullet;
import game.projectiles.Grenade;
import game.utils.Vector;

public class GrenadeLauncher extends Gun {
    private float aoeDistance = 0;
    
    public GrenadeLauncher() {
        super("/weapons/grenade-launcher.png");
        setFireRateInMillis(Config.DEFAULT_GRENADE_LAUNCHER_FIRE_RATE_MILLIS);
        setDamage(Config.DEFAULT_GRENADE_LAUNCHER_BULLET_DAMAGE);
        setAoeDistance(Config.DEFAULT_GRENADE_LAUNCHER_AOE_DISTANCE);
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
