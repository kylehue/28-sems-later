package game;

import game.powerups.PowerUpKind;
import game.utils.IntervalMap;
import game.utils.Vector;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

public abstract class Mechanics {
    private final static int PLAYER_MAX_XP_INCREASE = 50;
    private final static float PLAYER_HEALTH_INCREASE = 10;
    private final static int ZOMBIE_COUNT_INCREASE = 10;
    private final static float ZOMBIE_DAMAGE_INCREASE = 0.01f;
    
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
        Progress.maxXp.set(maxXp + PLAYER_MAX_XP_INCREASE);
        
        // Increase player hp
        Progress.currentHealth.set(Progress.currentHealth.get() + PLAYER_HEALTH_INCREASE);
        Progress.maxHealth.set(Progress.maxHealth.get() + PLAYER_HEALTH_INCREASE);
        
        // Increase zombie count
        Progress.zombieCount.set(Progress.zombieCount.get() + ZOMBIE_COUNT_INCREASE);
        
        // Increase zombie damage
        Progress.zombieDamage.set(Progress.zombieDamage.get() + ZOMBIE_DAMAGE_INCREASE);
        
        pickPowerUp();
    }
    
    private static void pickPowerUp() {
        Game.world.pause();
        
        Game.scene.setPowerUpSelectionComponentVisible(true);
        Game.scene.setOnSelectPowerUp(selectedPowerUp -> {
            selectedPowerUp.get().apply();
            Game.scene.setPowerUpSelectionComponentVisible(false);
            Game.world.play();
        });
    }
    
    private static void handleZombieSpawn() {
        if (!intervals.isIntervalOverFor(Interval.SPAWN_ZOMBIE)) return;
        intervals.resetIntervalFor(Interval.SPAWN_ZOMBIE);
        final int BATCH_SIZE = 50;
        int zombiesCount = Math.min(Progress.zombieCount.get(), Config.MAX_ZOMBIES_COUNT);
        if (Game.world.getZombies().size() < zombiesCount) {
            int spawnCount = Math.min(
                zombiesCount - Game.world.getZombies().size(),
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
