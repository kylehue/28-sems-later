package game.powerups;

import game.Progress;
import game.inventory.WeaponKind;
import game.inventory.weapons.Rifle;

public class RifleAccuracy implements PowerUp {
    public void apply() {
        if (!isAllowedToUse()) return;
        Rifle rifle = (Rifle) WeaponKind.RIFLE.get();
        rifle.setAccuracy(1);
    }
    
    public boolean isAllowedToUse() {
        return Progress.unlockedWeapons.contains(WeaponKind.RIFLE);
    }
}
