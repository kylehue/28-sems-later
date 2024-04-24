package game.powerups;

import game.Config;
import game.Progress;

public class MovementSpeed implements PowerUp {
    public void apply() {
        Progress.playerSpeed.set(Progress.playerSpeed.get() + 5);
    }
    
    public boolean isAllowedToUse() {
        return Progress.playerSpeed.get() < Config.MAX_PLAYER_SPEED;
    }
}
