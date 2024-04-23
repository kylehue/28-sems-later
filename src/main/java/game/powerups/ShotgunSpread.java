package game.powerups;

import game.Progress;
import game.weapons.WeaponKind;
import game.weapons.Shotgun;

public class ShotgunSpread implements PowerUp {
    public void apply() {
        if (!isAllowedToUse()) return;
        Shotgun shotgun = (Shotgun) WeaponKind.SHOTGUN.get();
        shotgun.setSpreadInRadians(1);
    }
    
    public boolean isAllowedToUse() {
        return Progress.unlockedWeapons.contains(WeaponKind.SHOTGUN);
    }
}
