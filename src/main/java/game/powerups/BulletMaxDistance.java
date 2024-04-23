package game.powerups;

import game.Progress;

public class BulletMaxDistance implements PowerUp {
    public void apply() {
        Progress.bulletMaxDistance.add(1);
    }
    
    public boolean isAllowedToUse() {
        return true;
    }
}
