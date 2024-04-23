package game.powerups;

import game.Progress;
import game.weapons.WeaponKind;
import game.weapons.Gun;
import game.weapons.Weapon;

public class FireRate implements PowerUp {
    public void apply() {
        for (WeaponKind weaponKind : Progress.unlockedWeapons) {
            Weapon weapon = weaponKind.get();
            if (!(weapon instanceof Gun gun)) continue;
            gun.setFireRateInMillis(1);
        }
    }
    
    public boolean isAllowedToUse() {
        return true;
    }
}
