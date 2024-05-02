package game.powerups;

import game.Config;
import game.Game;
import game.Progress;

public class MovementSpeed implements PowerUp {
    public void apply() {
        Progress.PLAYER_SPEED.set(Progress.PLAYER_SPEED.get() + 30);
        
        if (Progress.PLAYER_SPEED.get() > Config.MAX_PLAYER_SPEED) {
            Progress.PLAYER_SPEED.set(Config.MAX_PLAYER_SPEED);
        }
        
        Game.scene.getMessages().add("Your movement speed has been upgraded!");
    }
    
    public boolean isAllowedToUse() {
        return Progress.PLAYER_SPEED.get() < Config.MAX_PLAYER_SPEED;
    }
}
