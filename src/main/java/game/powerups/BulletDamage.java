package game.powerups;

import game.Progress;
import game.inventory.WeaponKind;

public class BulletDamage implements PowerUp {
    public void apply() {
        for (WeaponKind weaponKind : Progress.unlockedWeapons) {
            weaponKind.get().addDamage(1);
        }
    }
    
    public boolean isAllowedToUse() {
        return true;
    }
}
