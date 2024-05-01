package game.powerups;

import game.Config;
import game.Progress;

public class MovementSpeed implements PowerUp {
    public void apply() {
        Progress.PLAYER_SPEED.set(Progress.PLAYER_SPEED.get() + 10);
        
        if (Progress.PLAYER_SPEED.get() > Config.MAX_PLAYER_SPEED) {
            Progress.PLAYER_SPEED.set(Config.MAX_PLAYER_SPEED);
        }
    }
    
    public boolean isAllowedToUse() {
        return Progress.PLAYER_SPEED.get() < Config.MAX_PLAYER_SPEED;
    }
}
