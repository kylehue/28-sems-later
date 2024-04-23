package game.powerups;

import game.Progress;

public class BulletSpeed implements PowerUp {
    public void apply() {
        Progress.bulletSpeed.add(1);
    }
    
    public boolean isAllowedToUse() {
        return true;
    }
}