package game.powerups;

import game.Progress;

public class BulletPenetration implements PowerUp {
    public void apply() {
        Progress.bulletPenetration.add(1);
    }
    
    public boolean isAllowedToUse() {
        return true;
    }
}
