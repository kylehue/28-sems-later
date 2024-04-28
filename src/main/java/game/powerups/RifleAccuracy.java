package game.powerups;

import game.Config;
import game.Progress;
import game.weapons.WeaponKind;
import game.weapons.Rifle;

public class RifleAccuracy implements PowerUp {
    public void apply() {
        Rifle rifle = (Rifle) WeaponKind.RIFLE.get();
        rifle.setAccuracy(rifle.getAccuracy() + 0.025f);
    }
    
    public boolean isAllowedToUse() {
        Rifle rifle = (Rifle) WeaponKind.RIFLE.get();
        return Progress.UNLOCKED_WEAPONS.contains(WeaponKind.RIFLE) && rifle.getAccuracy() < Config.MAX_RIFLE_ACCURACY;
    }
}