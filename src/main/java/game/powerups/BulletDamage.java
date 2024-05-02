package game.powerups;

import game.Game;
import game.Progress;
import game.weapons.WeaponKind;

public class BulletDamage implements PowerUp {
    @Override
    public void apply() {
        StringBuilder message = new StringBuilder();
        message.append("Damage has increased for ");
        for (WeaponKind weaponKind : Progress.UNLOCKED_WEAPONS) {
            weaponKind.get().addDamage(6);
            message.append(weaponKind.name().toLowerCase()).append(", ");
        }
        
        if (!message.isEmpty()) {
            message.delete(message.length() - 2, message.length());
        }
        
        message.append("!");
        
        Game.scene.getMessages().add(message.toString());
    }
    
    @Override
    public boolean isAllowedToUse() {
        return true;
    }
}
