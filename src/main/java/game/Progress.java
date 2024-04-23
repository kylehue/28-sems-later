package game;

import game.inventory.WeaponKind;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
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
    
    public static final FloatProperty maxHealth = new SimpleFloatProperty();
    public static final FloatProperty movementSpeed = new SimpleFloatProperty();
    public static final FloatProperty dashInterval = new SimpleFloatProperty();
    public static final FloatProperty grenadeAoe = new SimpleFloatProperty();
    public static final FloatProperty healthRegen = new SimpleFloatProperty();
    public static final FloatProperty bulletPenetration = new SimpleFloatProperty();
    public static final FloatProperty bulletSpeed = new SimpleFloatProperty();
    public static final FloatProperty bulletMaxDistance = new SimpleFloatProperty();
}
