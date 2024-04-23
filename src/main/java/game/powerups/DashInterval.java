package game.powerups;

import game.Progress;

public class DashInterval implements PowerUp {
    public void apply() {
        Progress.dashInterval.add(1);
    }
    
    public boolean isAllowedToUse() {
        return true;
    }
}