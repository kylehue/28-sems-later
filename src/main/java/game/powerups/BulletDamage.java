package game.powerups;

import game.Progress;
import game.weapons.WeaponKind;

public class BulletDamage implements PowerUp {
    @Override
    public void apply() {
        for (WeaponKind weaponKind : WeaponKind.values()) {
            weaponKind.get().addDamage(5);
        }
    }
    
    @Override
    public boolean isAllowedToUse() {
        return true;
    }
}
