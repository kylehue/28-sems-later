package game.powerups;

import game.Progress;

public class HealthRegen implements PowerUp {
    public void apply() {
        Progress.PLAYER_HEALTH_REGEN_HEALTH.set(Progress.PLAYER_HEALTH_REGEN_HEALTH.get() + 0.001f);
    }
    
    public boolean isAllowedToUse() {
        return true;
    }
}
