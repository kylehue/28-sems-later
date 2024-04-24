package game.powerups;

import game.Config;
import game.Progress;

public class DashInterval implements PowerUp {
    public void apply() {
        Progress.dashInterval.set(Progress.dashInterval.get() - 10);
    }
    
    public boolean isAllowedToUse() {
        return Progress.dashInterval.get() > Config.MIN_PLAYER_DASH_INTERVAL_MILLIS;
    }
}