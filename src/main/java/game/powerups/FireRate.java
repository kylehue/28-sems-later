package game.powerups;

import game.Config;
import game.Progress;
import game.weapons.*;

import java.util.HashSet;

public class FireRate implements PowerUp {
    public void apply() {
        for (Gun gun : getUpgradableGuns()) {
            gun.setFireRateInMillis(gun.getFireRateInMillis() - 10);
        }
    }
    
    private HashSet<Gun> getUpgradableGuns() {
        HashSet<Gun> upgradableGuns = new HashSet<>();
        Pistol pistol = (Pistol) WeaponKind.PISTOL.get();
        if (pistol.getFireRateInMillis() > Config.MIN_PISTOL_FIRE_RATE_MILLIS) {
            upgradableGuns.add(pistol);
        }
        Rifle rifle = (Rifle) WeaponKind.RIFLE.get();
        if (rifle.getFireRateInMillis() > Config.MIN_RIFLE_FIRE_RATE_MILLIS) {
            upgradableGuns.add(rifle);
        }
        Shotgun shotgun = (Shotgun) WeaponKind.SHOTGUN.get();
        if (shotgun.getFireRateInMillis() > Config.MIN_SHOTGUN_FIRE_RATE_MILLIS) {
            upgradableGuns.add(shotgun);
        }
        Sniper sniper = (Sniper) WeaponKind.SNIPER.get();
        if (sniper.getFireRateInMillis() > Config.MIN_SNIPER_FIRE_RATE_MILLIS) {
            upgradableGuns.add(sniper);
        }
        GrenadeLauncher grenadeLauncher = (GrenadeLauncher) WeaponKind.GRENADE_LAUNCHER.get();
        if (grenadeLauncher.getFireRateInMillis() > Config.MIN_GRENADE_LAUNCHER_FIRE_RATE_MILLIS) {
            upgradableGuns.add(grenadeLauncher);
        }
        return upgradableGuns;
    }
    
    public boolean isAllowedToUse() {
        return !getUpgradableGuns().isEmpty();
    }
}
