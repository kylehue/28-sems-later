package game.powerups;

import game.Config;
import game.Game;
import game.Progress;
import game.weapons.*;

import java.util.HashSet;

public class BulletMaxDistance implements PowerUp {
    @Override
    public void apply() {
        StringBuilder message = new StringBuilder();
        message.append("Bullet distance has increased for ");
        for (Gun gun : getUpgradableGuns()) {
            if (gun instanceof Pistol pistol) {
                pistol.setMaxDistance(pistol.getMaxDistance() + 50);
                
                if (pistol.getMaxDistance() > Config.MAX_PISTOL_MAX_BULLET_DISTANCE) {
                    pistol.setMaxDistance(Config.MAX_PISTOL_MAX_BULLET_DISTANCE);
                }
                
                message.append("pistol, ");
            } else if (gun instanceof Rifle rifle) {
                rifle.setMaxDistance(rifle.getMaxDistance() + 50);
                
                if (rifle.getMaxDistance() > Config.MAX_RIFLE_MAX_BULLET_DISTANCE) {
                    rifle.setMaxDistance(Config.MAX_RIFLE_MAX_BULLET_DISTANCE);
                }
                
                message.append("rifle, ");
            } else if (gun instanceof Shotgun shotgun) {
                shotgun.setMaxDistance(shotgun.getMaxDistance() + 50);
                
                if (shotgun.getMaxDistance() > Config.MAX_SHOTGUN_MAX_BULLET_DISTANCE) {
                    shotgun.setMaxDistance(Config.MAX_SHOTGUN_MAX_BULLET_DISTANCE);
                }
                
                message.append("shotgun, ");
            }
        }
        
        if (!message.isEmpty()) {
            message.delete(message.length() - 2, message.length());
        }
        
        message.append("!");
        
        Game.scene.getMessages().add(message.toString());
    }
    
    private HashSet<Gun> getUpgradableGuns() {
        HashSet<Gun> upgradableGuns = new HashSet<>();
        Pistol pistol = (Pistol) WeaponKind.PISTOL.get();
        if (
            Progress.UNLOCKED_WEAPONS.contains(WeaponKind.PISTOL) &&
            pistol.getBulletSpeed() < Config.MAX_PISTOL_MAX_BULLET_DISTANCE
        ) {
            upgradableGuns.add(pistol);
        }
        Rifle rifle = (Rifle) WeaponKind.RIFLE.get();
        if (
            Progress.UNLOCKED_WEAPONS.contains(WeaponKind.RIFLE) &&
            rifle.getBulletSpeed() < Config.MAX_RIFLE_MAX_BULLET_DISTANCE
        ) {
            upgradableGuns.add(rifle);
        }
        Shotgun shotgun = (Shotgun) WeaponKind.SHOTGUN.get();
        if (
            Progress.UNLOCKED_WEAPONS.contains(WeaponKind.SHOTGUN) &&
            shotgun.getBulletSpeed() < Config.MAX_SHOTGUN_MAX_BULLET_DISTANCE
        ) {
            upgradableGuns.add(shotgun);
        }
        return upgradableGuns;
    }
    
    @Override
    public boolean isAllowedToUse() {
        return !getUpgradableGuns().isEmpty();
    }
}
