package game.powerups;

import game.Progress;
import game.inventory.WeaponKind;

public class HealthRegen implements PowerUp {
    public void apply() {
        Progress.healthRegen.add(1);
    }
    
    public boolean isAllowedToUse() {
        return true;
    }
}
