package game.powerups;

import game.Progress;

public class MaxHealth implements PowerUp {
    public void apply() {
        Progress.PLAYER_MAX_HEALTH.set(Progress.PLAYER_MAX_HEALTH.get() + 50);
    }
    
    public boolean isAllowedToUse() {
        return true;
    }
}
