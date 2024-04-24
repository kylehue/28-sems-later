package game;

import game.colliders.Collider;
import game.entity.Zombie;
import game.map.Layer;
import game.map.Material;
import game.powerups.PowerUpKind;
import game.utils.IntervalMap;
import game.utils.Vector;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

public abstract class Mechanics {
    private final static IntervalMap intervals = new IntervalMap();
    
    private enum Interval {
        SPAWN_ZOMBIE
    }
    
    private static void handleLevelingSystem() {
        int currentXp = Progress.currentXp.get();
        int maxXp = Progress.maxXp.get();
        if (currentXp < maxXp) return;
        
        // Level up
        Progress.currentLevel.set(Progress.currentLevel.get() + 1);
        Progress.currentXp.set(0);
        Progress.maxXp.set(maxXp + 50);
        
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
    
    private static void handleZombieSpawn() {
        if (!intervals.isIntervalOverFor(Interval.SPAWN_ZOMBIE)) return;
        intervals.resetIntervalFor(Interval.SPAWN_ZOMBIE);
        final int BATCH_SIZE = 50;
        if (Game.world.getZombies().size() < Config.MAX_ZOMBIES_COUNT) {
            int spawnCount = Math.min(
                Config.MAX_ZOMBIES_COUNT - Game.world.getZombies().size(),
                BATCH_SIZE
            );
            for (int i = 0; i < spawnCount; i++) {
                // Spawn anywhere outside camera
                Vector randomPosition = Game.world.generateRandomPosition();
                while (Game.world.getCamera().isInViewport(randomPosition, 50)) {
                    randomPosition.set(Game.world.generateRandomPosition());
                }
                
                Game.world.spawnZombie(randomPosition);
            }
        }
    }
    
    private static void setup() {
        intervals.registerIntervalFor(Interval.SPAWN_ZOMBIE, 100);
    }
    
    private static boolean ticked = false;
    
    public static void update() {
        if (!ticked) {
            setup();
            ticked = true;
        }
        handleLevelingSystem();
        handleZombieSpawn();
    }
}
