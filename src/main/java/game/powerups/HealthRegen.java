package game.powerups;

import game.Progress;

public class HealthRegen implements PowerUp {
    public void apply() {
        Progress.healthRegen.add(1);
    }
    
    public boolean isAllowedToUse() {
        return true;
    }
}
