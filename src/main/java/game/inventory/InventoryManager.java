package game.inventory;

import game.entity.Player;
import game.inventory.weapons.*;

import java.util.HashSet;

public class InventoryManager {
    private final Player player;
    private final HashSet<WeaponMap.Kind> unlockedWeapons = new HashSet<>();
    private Weapon currentWeapon = WeaponMap.get(WeaponMap.Kind.GRENADE_LAUNCHER);
    
    public InventoryManager(Player player) {
        this.player = player;
    }
    
    public void useWeapon(WeaponMap.Kind weaponKind) {
        if (!isWeaponUnlocked(weaponKind)) return;
        currentWeapon = WeaponMap.get(WeaponMap.Kind.RIFLE);
    }
    
    public void useItem() {
    
    }
    
    public void usePowerUp() {
    
    }
    
    public void unlockWeapon(WeaponMap.Kind weaponKind) {
        unlockedWeapons.add(weaponKind);
    }
    
    public boolean isWeaponUnlocked(WeaponMap.Kind weaponKind) {
        return unlockedWeapons.contains(weaponKind);
    }
    
    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }
}
