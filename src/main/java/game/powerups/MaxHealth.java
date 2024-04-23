package game.powerups;

import game.Progress;

public class MaxHealth implements PowerUp {
    public void apply() {
        Progress.maxHealth.add(1);
    }
    
    public boolean isAllowedToUse() {
        return true;
    }
}
