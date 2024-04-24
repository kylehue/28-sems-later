package game;

import game.powerups.PowerUpKind;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

public abstract class Mechanics {
    private static void handleLevelingSystem() {
        int currentXp = Progress.currentXp.get();
        int maxXp = Progress.maxXp.get();
        if (currentXp < maxXp) return;
        
        // Level up
        Progress.currentLevel.set(Progress.currentLevel.get() + 1);
        Progress.currentXp.set(0);
        Progress.maxXp.set((int) (maxXp * 1.2));
        
        pickPowerUp();
    }
    
    private static void pickPowerUp() {
        Game.world.pause();
        Game.ui.showPowerUpSelect();
        InvalidationListener listener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                Game.world.play();
                PowerUpKind selectedPowerUp = Game.ui
                    .getPowerUpSelect()
                    .selectedOptionProperty()
                    .get();
                if (selectedPowerUp != null) {
                    selectedPowerUp.get().apply();
                }
                Game.ui
                    .getPowerUpSelect()
                    .selectedOptionProperty()
                    .removeListener(this);
                Game.ui.getPowerUpSelect().hide();
            }
        };
        Game.ui.getPowerUpSelect().selectedOptionProperty().addListener(listener);
    }
    
    public static void update() {
        handleLevelingSystem();
    }
}
