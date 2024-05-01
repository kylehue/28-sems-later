package game;

import game.weapons.WeaponKind;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

public abstract class Progress {
    public static final ObservableSet<WeaponKind> UNLOCKED_WEAPONS = FXCollections.observableSet(
        WeaponKind.PISTOL,
        WeaponKind.SHOTGUN,
        WeaponKind.RIFLE,
        WeaponKind.GRENADE_LAUNCHER,
        WeaponKind.SNIPER
    );
    
    public static final IntegerProperty PLAYER_ZOMBIE_KILLS = new SimpleIntegerProperty();
    public static final IntegerProperty PLAYER_CURRENT_LEVEL = new SimpleIntegerProperty(
        Config.DEFAULT_PLAYER_LEVEL
    );
    public static final IntegerProperty PLAYER_MAX_LEVEL = new SimpleIntegerProperty(
        Config.DEFAULT_PLAYER_MAX_LEVEL
    );
    public static final IntegerProperty PLAYER_CURRENT_XP = new SimpleIntegerProperty(
        Config.DEFAULT_PLAYER_XP
    );
    public static final IntegerProperty PLAYER_MAX_XP = new SimpleIntegerProperty(
        Config.DEFAULT_PLAYER_MAX_XP
    );
    public static final FloatProperty PLAYER_CURRENT_HEALTH = new SimpleFloatProperty(
        Config.DEFAULT_PLAYER_HEALTH
    );
    public static final FloatProperty PLAYER_MAX_HEALTH = new SimpleFloatProperty(
        Config.DEFAULT_PLAYER_MAX_HEALTH
    );
    public static final FloatProperty PLAYER_HEALTH_REGEN_HEALTH = new SimpleFloatProperty(
        Config.DEFAULT_PLAYER_HEALTH_REGEN_HEALTH
    );
    public static final FloatProperty PLAYER_SPEED = new SimpleFloatProperty(
        Config.DEFAULT_PLAYER_SPEED
    );
    public static final FloatProperty PLAYER_DASH_INTERVAL = new SimpleFloatProperty(
        Config.DEFAULT_PLAYER_DASH_INTERVAL_MILLIS
    );
    
    public static final FloatProperty ZOMBIE_HEALTH = new SimpleFloatProperty(
        Config.DEFAULT_ZOMBIE_HEALTH
    );
    public static final FloatProperty ZOMBIE_SPEED = new SimpleFloatProperty(
        Config.DEFAULT_ZOMBIE_SPEED
    );
    public static final IntegerProperty ZOMBIE_COUNT = new SimpleIntegerProperty(
        Config.DEFAULT_ZOMBIE_COUNT
    );
    public static final FloatProperty ZOMBIE_DAMAGE = new SimpleFloatProperty(
        Config.DEFAULT_ZOMBIE_DAMAGE
    );
    
    public static final FloatProperty DEVIL_HEALTH = new SimpleFloatProperty(
        Config.DEFAULT_DEVIL_HEALTH
    );
    public static final FloatProperty DEVIL_SPEED = new SimpleFloatProperty(
        Config.DEFAULT_DEVIL_SPEED
    );
    public static final IntegerProperty DEVIL_COUNT = new SimpleIntegerProperty(
        Config.DEFAULT_DEVIL_COUNT
    );
    public static final FloatProperty DEVIL_DAMAGE = new SimpleFloatProperty(
        Config.DEFAULT_DEVIL_DAMAGE
    );
    
    public static void reset() {
        for (WeaponKind weaponKind : UNLOCKED_WEAPONS) {
            weaponKind.get().resetStats();
        }
        UNLOCKED_WEAPONS.clear();
        UNLOCKED_WEAPONS.add(WeaponKind.PISTOL);
        
        PLAYER_ZOMBIE_KILLS.set(0);
        PLAYER_CURRENT_LEVEL.set(Config.DEFAULT_PLAYER_LEVEL);
        PLAYER_MAX_LEVEL.set(Config.DEFAULT_PLAYER_MAX_LEVEL);
        PLAYER_CURRENT_XP.set(Config.DEFAULT_PLAYER_XP);
        PLAYER_MAX_XP.set(Config.DEFAULT_PLAYER_MAX_XP);
        PLAYER_CURRENT_HEALTH.set(Config.DEFAULT_PLAYER_HEALTH);
        PLAYER_MAX_HEALTH.set(Config.DEFAULT_PLAYER_MAX_HEALTH);
        PLAYER_HEALTH_REGEN_HEALTH.set(Config.DEFAULT_PLAYER_HEALTH_REGEN_HEALTH);
        PLAYER_SPEED.set(Config.DEFAULT_PLAYER_SPEED);
        PLAYER_DASH_INTERVAL.set(Config.DEFAULT_PLAYER_DASH_INTERVAL_MILLIS);
        
        ZOMBIE_SPEED.set(Config.DEFAULT_ZOMBIE_SPEED);
        ZOMBIE_COUNT.set(Config.DEFAULT_ZOMBIE_COUNT);
        ZOMBIE_HEALTH.set(Config.DEFAULT_ZOMBIE_HEALTH);
        ZOMBIE_DAMAGE.set(Config.DEFAULT_ZOMBIE_DAMAGE);
        
        DEVIL_SPEED.set(Config.DEFAULT_DEVIL_SPEED);
        DEVIL_COUNT.set(Config.DEFAULT_DEVIL_COUNT);
        DEVIL_HEALTH.set(Config.DEFAULT_DEVIL_HEALTH);
        DEVIL_DAMAGE.set(Config.DEFAULT_DEVIL_DAMAGE);
    }
}
