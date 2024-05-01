package game.powerups;

import game.Config;
import game.Progress;
import game.weapons.GrenadeLauncher;
import game.weapons.WeaponKind;

public class GrenadeAoe implements PowerUp {
    public void apply() {
        GrenadeLauncher grenadeLauncher = (GrenadeLauncher) WeaponKind.GRENADE_LAUNCHER.get();
        grenadeLauncher.setAoeDistance(grenadeLauncher.getAoeDistance() + 3.5f);
        
        if (grenadeLauncher.getAoeDistance() > Config.MAX_GRENADE_LAUNCHER_AOE_DISTANCE) {
            grenadeLauncher.setAoeDistance(Config.MAX_GRENADE_LAUNCHER_AOE_DISTANCE);
        }
    }
    
    public boolean isAllowedToUse() {
        GrenadeLauncher grenadeLauncher = (GrenadeLauncher) WeaponKind.GRENADE_LAUNCHER.get();
        return Progress.UNLOCKED_WEAPONS.contains(WeaponKind.GRENADE_LAUNCHER) && grenadeLauncher.getAoeDistance() < Config.MAX_GRENADE_LAUNCHER_AOE_DISTANCE;
    }
}