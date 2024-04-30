package game.weapons;

import game.Config;
import game.Game;
import game.World;
import game.projectiles.InstantBullet;
import game.utils.Vector;

public class Sniper extends Gun {
    private float penetration = 0;
    
    public Sniper() {
        super("/weapons/sniper.png");
        setMuzzlePosition(new Vector(52, 7));
        setOrigHandlePosition(new Vector(24, 10));
        resetStats();
    }
    
    public void setPenetration(float penetration) {
        this.penetration = penetration;
    }
    
    public float getPenetration() {
        return penetration;
    }
    
    @Override
    public void resetStats() {
        setFireRateInMillis(Config.DEFAULT_SNIPER_FIRE_RATE_MILLIS);
        setPenetration(Config.DEFAULT_SNIPER_BULLET_PENETRATION);
        setDamage(Config.DEFAULT_SNIPER_BULLET_DAMAGE);
    }
    
    @Override
    public void handleShoot(World world, Vector initialPosition, float angle) {
        InstantBullet bullet = world.spawnInstantBullet(initialPosition, angle);
        bullet.setPenetration(penetration);
        bullet.setDamage(damage);
        
        Game.world.addPlayerDistanceAwareAudio(
            "/sounds/sniper-shot.mp3",
            initialPosition,
            250
        );
    }
}
