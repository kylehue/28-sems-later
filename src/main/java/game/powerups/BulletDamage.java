package game.powerups;

import game.Progress;
import game.weapons.WeaponKind;

public class BulletDamage implements PowerUp {
    @Override
    public void apply() {
        for (WeaponKind weaponKind : Progress.unlockedWeapons) {
            weaponKind.get().addDamage(1);
        }
    }
    
    @Override
    public boolean isAllowedToUse() {
        return true;
    }
}
