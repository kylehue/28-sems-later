package game.powerups;

import game.Config;
import game.Progress;
import game.weapons.*;

import java.util.HashSet;

public class FireRate implements PowerUp {
    public void apply() {
        for (Gun gun : getUpgradableGuns()) {
            gun.setFireRateInMillis(gun.getFireRateInMillis() - 10);
            
            if (gun instanceof Pistol pistol) {
                if (pistol.getFireRateInMillis() < Config.MIN_PISTOL_FIRE_RATE_MILLIS) {
                    pistol.setFireRateInMillis(Config.MIN_PISTOL_FIRE_RATE_MILLIS);
                }
            } else if (gun instanceof Rifle rifle) {
                if (rifle.getFireRateInMillis() < Config.MIN_RIFLE_FIRE_RATE_MILLIS) {
                    rifle.setFireRateInMillis(Config.MIN_RIFLE_FIRE_RATE_MILLIS);
                }
            } else if (gun instanceof Shotgun shotgun) {
                if (shotgun.getFireRateInMillis() < Config.MIN_SHOTGUN_FIRE_RATE_MILLIS) {
                    shotgun.setFireRateInMillis(Config.MIN_SHOTGUN_FIRE_RATE_MILLIS);
                }
            } else if (gun instanceof Sniper sniper) {
                if (sniper.getFireRateInMillis() < Config.MIN_SNIPER_FIRE_RATE_MILLIS) {
                    sniper.setFireRateInMillis(Config.MIN_SNIPER_FIRE_RATE_MILLIS);
                }
            } else if (gun instanceof GrenadeLauncher grenadeLauncher) {
                if (grenadeLauncher.getFireRateInMillis() < Config.MIN_GRENADE_LAUNCHER_FIRE_RATE_MILLIS) {
                    grenadeLauncher.setFireRateInMillis(Config.MIN_GRENADE_LAUNCHER_FIRE_RATE_MILLIS);
                }
            }
        }
    }
    
    private HashSet<Gun> getUpgradableGuns() {
        HashSet<Gun> upgradableGuns = new HashSet<>();
        Pistol pistol = (Pistol) WeaponKind.PISTOL.get();
        if (
            Progress.UNLOCKED_WEAPONS.contains(WeaponKind.PISTOL) &&
                pistol.getFireRateInMillis() > Config.MIN_PISTOL_FIRE_RATE_MILLIS
        ) {
            upgradableGuns.add(pistol);
        }
        Rifle rifle = (Rifle) WeaponKind.RIFLE.get();
        if (
            Progress.UNLOCKED_WEAPONS.contains(WeaponKind.RIFLE) &&
            rifle.getFireRateInMillis() > Config.MIN_RIFLE_FIRE_RATE_MILLIS
        ) {
            upgradableGuns.add(rifle);
        }
        Shotgun shotgun = (Shotgun) WeaponKind.SHOTGUN.get();
        if (
            Progress.UNLOCKED_WEAPONS.contains(WeaponKind.SHOTGUN) &&
            shotgun.getFireRateInMillis() > Config.MIN_SHOTGUN_FIRE_RATE_MILLIS
        ) {
            upgradableGuns.add(shotgun);
        }
        Sniper sniper = (Sniper) WeaponKind.SNIPER.get();
        if (
            Progress.UNLOCKED_WEAPONS.contains(WeaponKind.SNIPER) &&
            sniper.getFireRateInMillis() > Config.MIN_SNIPER_FIRE_RATE_MILLIS
        ) {
            upgradableGuns.add(sniper);
        }
        GrenadeLauncher grenadeLauncher = (GrenadeLauncher) WeaponKind.GRENADE_LAUNCHER.get();
        if (
            Progress.UNLOCKED_WEAPONS.contains(WeaponKind.GRENADE_LAUNCHER) &&
            grenadeLauncher.getFireRateInMillis() > Config.MIN_GRENADE_LAUNCHER_FIRE_RATE_MILLIS
        ) {
            upgradableGuns.add(grenadeLauncher);
        }
        return upgradableGuns;
    }
    
    public boolean isAllowedToUse() {
        return !getUpgradableGuns().isEmpty();
    }
}
