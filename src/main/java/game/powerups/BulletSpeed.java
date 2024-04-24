package game.powerups;

import game.Config;
import game.Progress;
import game.weapons.*;

import java.util.HashSet;

public class BulletSpeed implements PowerUp {
    @Override
    public void apply() {
        for (Gun gun : getUpgradableGuns()) {
            if (gun instanceof Pistol pistol) {
                pistol.setBulletSpeed(pistol.getBulletSpeed() + 50);
            } else if (gun instanceof Rifle rifle) {
                rifle.setBulletSpeed(rifle.getBulletSpeed() + 50);
            } else if (gun instanceof Shotgun shotgun) {
                shotgun.setBulletSpeed(shotgun.getBulletSpeed() + 50);
            }
        }
    }
    
    private HashSet<Gun> getUpgradableGuns() {
        HashSet<Gun> upgradableGuns = new HashSet<>();
        Pistol pistol = (Pistol) WeaponKind.PISTOL.get();
        if (pistol.getBulletSpeed() < Config.MAX_PISTOL_BULLET_SPEED) {
            upgradableGuns.add(pistol);
        }
        Rifle rifle = (Rifle) WeaponKind.RIFLE.get();
        if (rifle.getBulletSpeed() < Config.MAX_RIFLE_BULLET_SPEED) {
            upgradableGuns.add(rifle);
        }
        Shotgun shotgun = (Shotgun) WeaponKind.SHOTGUN.get();
        if (shotgun.getBulletSpeed() < Config.MAX_SHOTGUN_BULLET_SPEED) {
            upgradableGuns.add(shotgun);
        }
        return upgradableGuns;
    }
    
    @Override
    public boolean isAllowedToUse() {
        return !getUpgradableGuns().isEmpty();
    }
}