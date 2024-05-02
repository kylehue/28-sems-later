package game.powerups;

import game.Config;
import game.Game;
import game.Progress;
import game.weapons.*;

import java.util.HashSet;

public class BulletPenetration implements PowerUp {
    @Override
    public void apply() {
        StringBuilder message = new StringBuilder();
        message.append("Bullet piercing has increased for ");
        for (Gun gun : getUpgradableGuns()) {
            if (gun instanceof Pistol pistol) {
                pistol.setPenetration(pistol.getPenetration() + 0.11f);
                
                if (pistol.getPenetration() > Config.MAX_PISTOL_BULLET_PENETRATION) {
                    pistol.setPenetration(Config.MAX_PISTOL_BULLET_PENETRATION);
                }
                
                message.append("pistol, ");
            } else if (gun instanceof Rifle rifle) {
                rifle.setPenetration(rifle.getPenetration() + 0.21f);
                
                if (rifle.getPenetration() > Config.MAX_RIFLE_BULLET_PENETRATION) {
                    rifle.setPenetration(Config.MAX_RIFLE_BULLET_PENETRATION);
                }
                
                message.append("rifle, ");
            } else if (gun instanceof Shotgun shotgun) {
                shotgun.setPenetration(shotgun.getPenetration() + 0.21f);
                
                if (shotgun.getPenetration() > Config.MAX_SHOTGUN_BULLET_PENETRATION) {
                    shotgun.setPenetration(Config.MAX_SHOTGUN_BULLET_PENETRATION);
                }
                
                message.append("shotgun, ");
            } else if (gun instanceof Sniper sniper) {
                sniper.setPenetration(sniper.getPenetration() + 2.1f);
                
                if (sniper.getPenetration() > Config.MAX_SNIPER_BULLET_PENETRATION) {
                    sniper.setPenetration(Config.MAX_SNIPER_BULLET_PENETRATION);
                }
                
                message.append("sniper, ");
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
            pistol.getPenetration() < Config.MAX_PISTOL_BULLET_PENETRATION
        ) {
            upgradableGuns.add(pistol);
        }
        Rifle rifle = (Rifle) WeaponKind.RIFLE.get();
        if (
            Progress.UNLOCKED_WEAPONS.contains(WeaponKind.RIFLE) &&
            rifle.getPenetration() < Config.MAX_RIFLE_BULLET_PENETRATION
        ) {
            upgradableGuns.add(rifle);
        }
        Shotgun shotgun = (Shotgun) WeaponKind.SHOTGUN.get();
        if (
            Progress.UNLOCKED_WEAPONS.contains(WeaponKind.SHOTGUN) &&
            shotgun.getPenetration() < Config.MAX_SHOTGUN_BULLET_PENETRATION
        ) {
            upgradableGuns.add(shotgun);
        }
        Sniper sniper = (Sniper) WeaponKind.SNIPER.get();
        if (
            Progress.UNLOCKED_WEAPONS.contains(WeaponKind.SNIPER) &&
            sniper.getPenetration() < Config.MAX_SNIPER_BULLET_PENETRATION
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
