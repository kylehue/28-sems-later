package game;

import game.entity.Player;
import game.weapons.Weapon;
import game.weapons.WeaponKind;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class InventoryManager {
    private final Player player;
    private final ObjectProperty<WeaponKind> currentWeapon = new SimpleObjectProperty<>(WeaponKind.PISTOL);
    
    public InventoryManager(Player player) {
        this.player = player;
    }
    
    public void useWeapon(WeaponKind weaponKind) {
        if (!Progress.unlockedWeapons.contains(weaponKind)) return;
        currentWeapon.set(weaponKind);
    }
    
    public void useItem() {
    
    }
    
    public void usePowerUp() {
    
    }
    
    public Weapon getCurrentWeapon() {
        return currentWeapon.get().get();
    }
    
    public void setCurrentWeapon(WeaponKind weaponKind) {
        currentWeapon.set(weaponKind);
    }
    
    public ObjectProperty<WeaponKind> currentWeaponProperty() {
        return currentWeapon;
    }
}
