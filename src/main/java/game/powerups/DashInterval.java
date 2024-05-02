package game.powerups;

import game.Config;
import game.Progress;

public class DashInterval implements PowerUp {
    public void apply() {
        Progress.PLAYER_DASH_INTERVAL.set(Progress.PLAYER_DASH_INTERVAL.get() - 100);
        
        if (Progress.PLAYER_DASH_INTERVAL.get() < Config.MIN_PLAYER_DASH_INTERVAL_MILLIS) {
            Progress.PLAYER_DASH_INTERVAL.set(Config.MIN_PLAYER_DASH_INTERVAL_MILLIS);
        }
    }
    
    public boolean isAllowedToUse() {
        return Progress.PLAYER_DASH_INTERVAL.get() > Config.MIN_PLAYER_DASH_INTERVAL_MILLIS;
    }
}