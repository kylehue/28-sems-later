package game.powerups;

import game.Config;
import game.Game;
import game.Progress;

public class HealthRegen implements PowerUp {
    public void apply() {
        Progress.PLAYER_HEALTH_REGEN_HEALTH.set(Progress.PLAYER_HEALTH_REGEN_HEALTH.get() + 0.02f);
        
        if (Progress.PLAYER_HEALTH_REGEN_HEALTH.get() > Config.MAX_PLAYER_HEALTH_REGEN_HEALTH) {
            Progress.PLAYER_HEALTH_REGEN_HEALTH.set(
                Config.MAX_PLAYER_HEALTH_REGEN_HEALTH
            );
        }
        
        Game.scene.getMessages().add("Your health regen has been upgraded!");
    }
    
    public boolean isAllowedToUse() {
        return Progress.PLAYER_HEALTH_REGEN_HEALTH.get() < Config.MAX_PLAYER_HEALTH_REGEN_HEALTH;
    }
}
