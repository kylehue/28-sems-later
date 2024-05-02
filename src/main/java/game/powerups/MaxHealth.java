package game.powerups;

import game.Game;
import game.Progress;

public class MaxHealth implements PowerUp {
    public void apply() {
        Progress.PLAYER_MAX_HEALTH.set(Progress.PLAYER_MAX_HEALTH.get() + 50);
        
        Game.scene.getMessages().add("Your max health has increased!");
    }
    
    public boolean isAllowedToUse() {
        return true;
    }
}
