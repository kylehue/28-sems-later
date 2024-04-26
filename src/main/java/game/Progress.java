package game;

import game.weapons.WeaponKind;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

public abstract class Progress {
    public static final ObservableSet<WeaponKind> unlockedWeapons = FXCollections.observableSet(
        WeaponKind.PISTOL,
        WeaponKind.SHOTGUN,
        WeaponKind.RIFLE,
        WeaponKind.GRENADE_LAUNCHER,
        WeaponKind.SNIPER
    );
    
    public static final IntegerProperty currentLevel = new SimpleIntegerProperty(
        Config.DEFAULT_PLAYER_LEVEL
    );
    public static final IntegerProperty maxLevel = new SimpleIntegerProperty(
        Config.DEFAULT_PLAYER_MAX_LEVEL
    );
    public static final IntegerProperty currentXp = new SimpleIntegerProperty(
        Config.DEFAULT_PLAYER_XP
    );
    public static final IntegerProperty maxXp = new SimpleIntegerProperty(
        Config.DEFAULT_PLAYER_MAX_XP
    );
    public static final FloatProperty currentHealth = new SimpleFloatProperty(
        Config.DEFAULT_PLAYER_HEALTH
    );
    public static final FloatProperty maxHealth = new SimpleFloatProperty(
        Config.DEFAULT_PLAYER_MAX_HEALTH
    );
    public static final FloatProperty healthRegenHealth = new SimpleFloatProperty(
        Config.DEFAULT_PLAYER_HEALTH_REGEN_HEALTH
    );
    public static final FloatProperty playerSpeed = new SimpleFloatProperty(
        Config.DEFAULT_PLAYER_SPEED
    );
    public static final FloatProperty dashInterval = new SimpleFloatProperty(
        Config.DEFAULT_PLAYER_DASH_INTERVAL_MILLIS
    );
    
    // Zombie stats
    public static final FloatProperty zombieSpeed = new SimpleFloatProperty(
        Config.DEFAULT_ZOMBIE_SPEED
    );
    public static final IntegerProperty zombieCount = new SimpleIntegerProperty(
        Config.DEFAULT_ZOMBIES_COUNT
    );
    public static final FloatProperty zombieDamage = new SimpleFloatProperty(
        Config.DEFAULT_ZOMBIE_DAMAGE
    );
}
