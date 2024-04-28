package game.powerups;

import game.Config;
import game.Progress;
import game.weapons.*;

import java.util.HashSet;

public class BulletPenetration implements PowerUp {
    @Override
    public void apply() {
        for (Gun gun : getUpgradableGuns()) {
            if (gun instanceof Pistol pistol) {
                pistol.setPenetration(pistol.getPenetration() + 0.001f);
            } else if (gun instanceof Rifle rifle) {
                rifle.setPenetration(rifle.getPenetration() + 0.001f);
            } else if (gun instanceof Shotgun shotgun) {
                shotgun.setPenetration(shotgun.getPenetration() + 0.001f);
            } else if (gun instanceof Sniper sniper) {
                sniper.setPenetration(sniper.getPenetration() + 0.1f);
            }
        }
    }
    
    private HashSet<Gun> getUpgradableGuns() {
        HashSet<Gun> upgradableGuns = new HashSet<>();
        Pistol pistol = (Pistol) WeaponKind.PISTOL.get();
        if (
            Progress.UNLOCKED_WEAPONS.contains(WeaponKind.PISTOL) &&
            pistol.getPenetration() < Config.MAX_BULLET_PENETRATION
        ) {
            upgradableGuns.add(pistol);
        }
        Rifle rifle = (Rifle) WeaponKind.RIFLE.get();
        if (
            Progress.UNLOCKED_WEAPONS.contains(WeaponKind.RIFLE) &&
            rifle.getPenetration() < Config.MAX_BULLET_PENETRATION
        ) {
            upgradableGuns.add(rifle);
        }
        Shotgun shotgun = (Shotgun) WeaponKind.SHOTGUN.get();
        if (
            Progress.UNLOCKED_WEAPONS.contains(WeaponKind.SHOTGUN) &&
            shotgun.getPenetration() < Config.MAX_BULLET_PENETRATION
        ) {
            upgradableGuns.add(shotgun);
        }
        Sniper sniper = (Sniper) WeaponKind.SNIPER.get();
        if (
            Progress.UNLOCKED_WEAPONS.contains(WeaponKind.SNIPER) &&
            sniper.getPenetration() < Config.MAX_INSTANT_BULLET_PENETRATION
        ) {
            upgradableGuns.add(sniper);
        }
        return upgradableGuns;
    }
    
    @Override
    public boolean isAllowedToUse() {
        return !getUpgradableGuns().isEmpty();
    }
}
