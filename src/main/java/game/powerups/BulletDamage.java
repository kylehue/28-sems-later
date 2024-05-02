package game.powerups;

import game.Progress;
import game.weapons.WeaponKind;

public class BulletDamage implements PowerUp {
    @Override
    public void apply() {
        for (WeaponKind weaponKind : Progress.UNLOCKED_WEAPONS) {
            weaponKind.get().addDamage(6);
        }
    }
    
    @Override
    public boolean isAllowedToUse() {
        return true;
    }
}
