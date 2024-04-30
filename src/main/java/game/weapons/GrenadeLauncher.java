package game.weapons;

import game.Config;
import game.Game;
import game.World;
import game.projectiles.Grenade;
import game.utils.Vector;

public class GrenadeLauncher extends Gun {
    private float aoeDistance = 0;
    
    public GrenadeLauncher() {
        super("/weapons/grenade-launcher.png");
        setMuzzlePosition(new Vector(41, 4));
        setOrigHandlePosition(new Vector(24, 7));
        setMuzzleFlashEnabled(false);
        resetStats();
    }
    
    public void setAoeDistance(float aoeDistance) {
        this.aoeDistance = aoeDistance;
    }
    
    public float getAoeDistance() {
        return aoeDistance;
    }
    
    @Override
    public void resetStats() {
        setFireRateInMillis(Config.DEFAULT_GRENADE_LAUNCHER_FIRE_RATE_MILLIS);
        setDamage(Config.DEFAULT_GRENADE_LAUNCHER_GRENADE_DAMAGE);
        setAoeDistance(Config.DEFAULT_GRENADE_LAUNCHER_AOE_DISTANCE);
    }
    
    @Override
    public void handleShoot(World world, Vector initialPosition, float angle) {
        Grenade grenade = world.spawnGrenade(initialPosition, angle);
        grenade.setDamage(damage);
        grenade.setAoeDistance(aoeDistance);
    }
    
    @Override
    protected void handleSound(Vector initialPosition) {
        Game.world.addPlayerDistanceAwareAudio(
            "/sounds/grenade-launcher-shot.mp3",
            initialPosition,
            300
        );
    }
}
