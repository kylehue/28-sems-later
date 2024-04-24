package game.powerups;

import game.Progress;

public class MaxHealth implements PowerUp {
    public void apply() {
        Progress.maxHealth.set(Progress.maxHealth.get() + 50);
    }
    
    public boolean isAllowedToUse() {
        return true;
    }
}
