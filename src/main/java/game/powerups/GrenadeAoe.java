package game.powerups;

import game.Progress;

public class GrenadeAoe implements PowerUp {
    public void apply() {
        Progress.grenadeAoe.add(1);
    }
    
    public boolean isAllowedToUse() {
        return true;
    }
}