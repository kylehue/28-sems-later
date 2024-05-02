package game.powerups;

import game.Config;
import game.Game;
import game.Progress;

public class DashInterval implements PowerUp {
    public void apply() {
        Progress.PLAYER_DASH_INTERVAL.set(Progress.PLAYER_DASH_INTERVAL.get() - 100);
        
        if (Progress.PLAYER_DASH_INTERVAL.get() < Config.MIN_PLAYER_DASH_INTERVAL_MILLIS) {
            Progress.PLAYER_DASH_INTERVAL.set(Config.MIN_PLAYER_DASH_INTERVAL_MILLIS);
        }
        
        Game.scene.getMessages().add("Your dash cooldown has been upgraded!");
    }
    
    public boolean isAllowedToUse() {
        return Progress.PLAYER_DASH_INTERVAL.get() > Config.MIN_PLAYER_DASH_INTERVAL_MILLIS;
    }
}