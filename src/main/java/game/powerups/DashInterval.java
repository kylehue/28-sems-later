package game.powerups;

import game.Config;
import game.Progress;

public class DashInterval implements PowerUp {
    public void apply() {
        Progress.PLAYER_DASH_INTERVAL.set(Progress.PLAYER_DASH_INTERVAL.get() - 10);
    }
    
    public boolean isAllowedToUse() {
        return Progress.PLAYER_DASH_INTERVAL.get() > Config.MIN_PLAYER_DASH_INTERVAL_MILLIS;
    }
}