package game.powerups;

import game.Progress;

public class HealthRegen implements PowerUp {
    public void apply() {
        Progress.healthRegenHealth.set(Progress.healthRegenHealth.get() + 0.01f);
    }
    
    public boolean isAllowedToUse() {
        return true;
    }
}
