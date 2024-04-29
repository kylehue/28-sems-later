package game;

public abstract class Config {
    // Grenade defaults
    public final static int DEFAULT_GRENADE_DETONATION_TIME_MILLIS = 1000;
    public final static float DEFAULT_GRENADE_AOE_DISTANCE = 60;
    
    // Pistol defaults
    public final static int DEFAULT_PISTOL_FIRE_RATE_MILLIS = 300;
    public final static float MIN_PISTOL_FIRE_RATE_MILLIS = 150;
    public final static float DEFAULT_PISTOL_BULLET_DAMAGE = 30;
    public final static float DEFAULT_PISTOL_BULLET_MAX_DISTANCE = 200;
    public final static float MAX_PISTOL_MAX_BULLET_DISTANCE = 400;
    public final static float DEFAULT_PISTOL_BULLET_SPEED = 15000;
    public final static float MAX_PISTOL_BULLET_SPEED = 25000;
    public final static float DEFAULT_PISTOL_BULLET_PENETRATION = 1;
    
    // Rifle defaults
    public final static int DEFAULT_RIFLE_FIRE_RATE_MILLIS = 100;
    public final static float MIN_RIFLE_FIRE_RATE_MILLIS = 5;
    public final static float DEFAULT_RIFLE_BULLET_DAMAGE = 15;
    public final static float DEFAULT_RIFLE_BULLET_MAX_DISTANCE = 200;
    public final static float MAX_RIFLE_MAX_BULLET_DISTANCE = 400;
    public final static float DEFAULT_RIFLE_BULLET_SPEED = 15000;
    public final static float MAX_RIFLE_BULLET_SPEED = 25000;
    public final static float DEFAULT_RIFLE_BULLET_PENETRATION = 1;
    public final static float DEFAULT_RIFLE_ACCURACY = 0; /* 0-1 */
    public final static float MAX_RIFLE_ACCURACY = 0.8f; /* 0-1 */
    
    // Shotgun defaults
    public final static int DEFAULT_SHOTGUN_FIRE_RATE_MILLIS = 500;
    public final static float MIN_SHOTGUN_FIRE_RATE_MILLIS = 250;
    public final static float DEFAULT_SHOTGUN_BULLET_DAMAGE = 10;
    public final static float DEFAULT_SHOTGUN_BULLET_MAX_DISTANCE = 200;
    public final static float MAX_SHOTGUN_MAX_BULLET_DISTANCE = 400;
    public final static float DEFAULT_SHOTGUN_BULLET_SPEED = 15000;
    public final static float MAX_SHOTGUN_BULLET_SPEED = 25000;
    public final static float DEFAULT_SHOTGUN_BULLET_PENETRATION = 1;
    public final static float DEFAULT_SHOTGUN_SPREAD_RADIANS = (float) (Math.PI / 4);
    public final static float MAX_SHOTGUN_SPREAD_RADIANS = (float) (Math.PI / 2);
    
    // Sniper defaults
    public final static int DEFAULT_SNIPER_FIRE_RATE_MILLIS = 1400;
    public final static float MIN_SNIPER_FIRE_RATE_MILLIS = 1000;
    public final static float DEFAULT_SNIPER_BULLET_DAMAGE = 100;
    public final static float DEFAULT_SNIPER_BULLET_PENETRATION = 5.5f;
    
    // Grenade Launcher defaults
    public final static int DEFAULT_GRENADE_LAUNCHER_FIRE_RATE_MILLIS = 900;
    public final static float MIN_GRENADE_LAUNCHER_FIRE_RATE_MILLIS = 700;
    public final static float DEFAULT_GRENADE_LAUNCHER_GRENADE_DAMAGE = 80;
    public final static float DEFAULT_GRENADE_LAUNCHER_AOE_DISTANCE = 60;
    public final static float MAX_GRENADE_LAUNCHER_AOE_DISTANCE = 120;
    
    // Player
    public final static int DEFAULT_PLAYER_DASH_INTERVAL_MILLIS = 2000;
    public final static float MIN_PLAYER_DASH_INTERVAL_MILLIS = 1000;
    public final static float DEFAULT_PLAYER_SPEED = 1000;
    public final static float MAX_PLAYER_SPEED = 1200;
    public final static float DEFAULT_PLAYER_DASH_SPEED = 15000;
    public final static float DEFAULT_PLAYER_MAX_HEALTH = 100;
    public final static float DEFAULT_PLAYER_HEALTH = 100;
    public final static float DEFAULT_PLAYER_HEALTH_REGEN_HEALTH = 0.05f;
    public final static int DEFAULT_PLAYER_MAX_XP = 50;
    public final static int DEFAULT_PLAYER_XP = 0;
    public final static int DEFAULT_PLAYER_MAX_LEVEL = 9999;
    public final static int DEFAULT_PLAYER_LEVEL = 1;
    
    // Zombie
    public final static float DEFAULT_ZOMBIE_DAMAGE = 1;
    public final static float DEFAULT_ZOMBIE_SPEED = 200;
    public final static float MAX_ZOMBIE_SPEED = 700;
    
    // Projectiles max stats
    public final static float MAX_BULLET_PENETRATION = 3;
    public final static float MAX_INSTANT_BULLET_PENETRATION = 20;
    
    // World
    public final static int DEFAULT_ZOMBIES_COUNT = 100;
    public final static int MAX_ZOMBIES_COUNT = 700;
}
