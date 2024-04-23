package game.powerups;

public enum PowerUpKind {
    HEALTH_REGEN(new HealthRegen()),
    MAX_HEALTH(new MaxHealth()),
    MOVEMENT_SPEED(new MovementSpeed()),
    DASH_INTERVAL(new DashInterval()),
    FIRE_RATE(new FireRate()),
    GRENADE_AOE(new GrenadeAoe()),
    SHOTGUN_SPREAD(new ShotgunSpread()),
    RIFLE_ACCURACY(new RifleAccuracy()),
    BULLET_DAMAGE(new BulletDamage()),
    BULLET_PENETRATION(new BulletPenetration()),
    BULLET_SPEED(new BulletSpeed()),
    BULLET_MAX_DISTANCE(new BulletMaxDistance());
    
    private final PowerUp runnable;
    
    PowerUpKind(PowerUp weapon) {
        this.runnable = weapon;
    }
    
    public PowerUp get() {
        return runnable;
    }
}
