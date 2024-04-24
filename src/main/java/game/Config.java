package game;

public abstract class Config {
    // Projectiles
    // Bullet defaults
    public final static float DEFAULT_BULLET_KNOCK_BACK_FORCE = 3000;
    public final static float DEFAULT_BULLET_SPEED = 20000;
    public final static float DEFAULT_BULLET_MAX_DISTANCE = 200;
    public final static float DEFAULT_BULLET_PENETRATION = 1;
    
    // Grenade defaults
    public final static int DEFAULT_GRENADE_DETONATION_TIME_MILLIS = 1000;
    public final static float DEFAULT_GRENADE_KNOCK_BACK_FORCE = 15000;
    public final static float DEFAULT_GRENADE_AOE_DISTANCE = 60;
    
    // Instant Bullet defaults
    public final static float DEFAULT_INSTANT_BULLET_KNOCK_BACK_FORCE = 10000;
    public final static float DEFAULT_INSTANT_BULLET_PENETRATION = 10.5f;
    
    // Guns
    // Pistol defaults
    public final static int DEFAULT_PISTOL_FIRE_RATE_MILLIS = 250;
    public final static float DEFAULT_PISTOL_BULLET_DAMAGE = 20;
    public final static float DEFAULT_PISTOL_BULLET_MAX_DISTANCE = 200;
    public final static float DEFAULT_PISTOL_BULLET_SPEED = 15000;
    
    // Rifle defaults
    public final static int DEFAULT_RIFLE_FIRE_RATE_MILLIS = 35;
    public final static float DEFAULT_RIFLE_BULLET_DAMAGE = 15;
    public final static float DEFAULT_RIFLE_BULLET_MAX_DISTANCE = 200;
    public final static float DEFAULT_RIFLE_BULLET_SPEED = 15000;
    public final static float DEFAULT_RIFLE_ACCURACY = 0; /* 0-1 */
    
    // Shotgun defaults
    public final static int DEFAULT_SHOTGUN_FIRE_RATE_MILLIS = 300;
    public final static float DEFAULT_SHOTGUN_BULLET_DAMAGE = 10;
    public final static float DEFAULT_SHOTGUN_BULLET_MAX_DISTANCE = 200;
    public final static float DEFAULT_SHOTGUN_BULLET_SPEED = 15000;
    public final static float DEFAULT_SHOTGUN_SPREAD_RADIANS = (float) (Math.PI / 4);
    
    // Sniper defaults
    public final static int DEFAULT_SNIPER_FIRE_RATE_MILLIS = 600;
    public final static float DEFAULT_SNIPER_BULLET_DAMAGE = 100;
    public final static float DEFAULT_SNIPER_BULLET_PENETRATION = 10.5f;
    
    // Grenade Launcher defaults
    public final static int DEFAULT_GRENADE_LAUNCHER_FIRE_RATE_MILLIS = 400;
    public final static float DEFAULT_GRENADE_LAUNCHER_BULLET_DAMAGE = 30;
    public final static float DEFAULT_GRENADE_LAUNCHER_AOE_DISTANCE = 60;
    
    // Player
    public final static int DEFAULT_PLAYER_DASH_INTERVAL_MILLIS = 2000;
    public final static float DEFAULT_PLAYER_SPEED = 1000;
    public final static float DEFAULT_PLAYER_DASH_SPEED = 15000;
    public final static float DEFAULT_PLAYER_MAX_HEALTH = 100;
    public final static float DEFAULT_PLAYER_HEALTH = 100;
    public final static int DEFAULT_PLAYER_MAX_XP = 100;
    public final static int DEFAULT_PLAYER_XP = 50;
    public final static int DEFAULT_PLAYER_MAX_LEVEL = 100;
    public final static int DEFAULT_PLAYER_LEVEL = 1;
    
    // Zombie
    public final static float DEFAULT_ZOMBIE_DAMAGE = 1;
}
