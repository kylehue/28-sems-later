package game.powerups;

import game.Config;
import game.Game;
import game.Progress;
import game.weapons.GrenadeLauncher;
import game.weapons.WeaponKind;

public class GrenadeAoe implements PowerUp {
    public void apply() {
        GrenadeLauncher grenadeLauncher = (GrenadeLauncher) WeaponKind.GRENADE_LAUNCHER.get();
        grenadeLauncher.setAoeDistance(grenadeLauncher.getAoeDistance() + 4);
        
        if (grenadeLauncher.getAoeDistance() > Config.MAX_GRENADE_LAUNCHER_AOE_DISTANCE) {
            grenadeLauncher.setAoeDistance(Config.MAX_GRENADE_LAUNCHER_AOE_DISTANCE);
        }
        
        Game.scene.getMessages().add("The grenade launcher's AOE has been upgraded!");
    }
    
    public boolean isAllowedToUse() {
        GrenadeLauncher grenadeLauncher = (GrenadeLauncher) WeaponKind.GRENADE_LAUNCHER.get();
        return Progress.UNLOCKED_WEAPONS.contains(WeaponKind.GRENADE_LAUNCHER) && grenadeLauncher.getAoeDistance() < Config.MAX_GRENADE_LAUNCHER_AOE_DISTANCE;
    }
}