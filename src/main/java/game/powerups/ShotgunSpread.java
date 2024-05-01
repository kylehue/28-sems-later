package game.powerups;

import game.Config;
import game.Progress;
import game.weapons.WeaponKind;
import game.weapons.Shotgun;

public class ShotgunSpread implements PowerUp {
    public void apply() {
        Shotgun shotgun = (Shotgun) WeaponKind.SHOTGUN.get();
        shotgun.setSpreadInRadians(
            (float) (shotgun.getSpreadInRadians() + Math.PI / 64)
        );
        
        if (shotgun.getSpreadInRadians() > Config.MAX_SHOTGUN_SPREAD_RADIANS) {
            shotgun.setSpreadInRadians(Config.MAX_SHOTGUN_SPREAD_RADIANS);
        }
    }
    
    public boolean isAllowedToUse() {
        Shotgun shotgun = (Shotgun) WeaponKind.SHOTGUN.get();
        return Progress.UNLOCKED_WEAPONS.contains(WeaponKind.SHOTGUN) && shotgun.getSpreadInRadians() < Config.MAX_SHOTGUN_SPREAD_RADIANS;
    }
}
