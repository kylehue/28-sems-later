package game;

import game.utils.IntervalMap;
import game.utils.Vector;

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
        int currentXp = Progress.PLAYER_CURRENT_XP.get();
        int maxXp = Progress.PLAYER_MAX_XP.get();
        if (currentXp < maxXp) return;
        
        // Level up
        Progress.PLAYER_CURRENT_LEVEL.set(Progress.PLAYER_CURRENT_LEVEL.get() + 1);
        Progress.PLAYER_CURRENT_XP.set(0);
        Progress.PLAYER_MAX_XP.set(maxXp + PLAYER_MAX_XP_INCREASE);
        
        // Increase player hp
        Progress.PLAYER_CURRENT_HEALTH.set(Progress.PLAYER_CURRENT_HEALTH.get() + PLAYER_HEALTH_INCREASE);
        Progress.PLAYER_MAX_HEALTH.set(Progress.PLAYER_MAX_HEALTH.get() + PLAYER_HEALTH_INCREASE);
        
        // Increase zombie count
        Progress.ZOMBIE_COUNT.set(Progress.ZOMBIE_COUNT.get() + ZOMBIE_COUNT_INCREASE);
        
        // Increase zombie damage
        Progress.ZOMBIE_DAMAGE.set(Progress.ZOMBIE_DAMAGE.get() + ZOMBIE_DAMAGE_INCREASE);
        
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
        int zombiesCount = Math.min(Progress.ZOMBIE_COUNT.get(), Config.MAX_ZOMBIES_COUNT);
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
    
    private static void handleGameOver() {
        if (Game.world.getPlayer().getCurrentHealth() <= 0) {
            gameOver();
        }
    }
    
    private static void gameOver() {
        Game.scene.setGameOverComponentVisible(true);
        Game.scene.setPowerUpSelectionComponentVisible(false);
        Game.scene.setWeaponSwitchComponentVisible(false);
        Game.scene.setOtherGameComponentsVisible(false);
        Game.world.gameOver();
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
        handleGameOver();
    }
}
