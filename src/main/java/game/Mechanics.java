package game;

import game.utils.IntervalMap;
import game.utils.Vector;
import game.weapons.WeaponKind;

import java.util.HashMap;

public abstract class Mechanics {
    private final static int PLAYER_MAX_XP_INCREASE_PER_LEVEL = 15;
    private final static float PLAYER_HEALTH_INCREASE_PER_LEVEL = 5;
    private final static int ZOMBIE_COUNT_INCREASE_PER_LEVEL = 35;
    private final static float ZOMBIE_DAMAGE_INCREASE_PER_LEVEL = 0.2f;
    private final static float ZOMBIE_SPEED_INCREASE_PER_LEVEL = 4;
    private final static int DEVIL_COUNT_INCREASE_PER_5_LEVELS = 1;
    private final static float DEVIL_DAMAGE_INCREASE_PER_LEVEL = 1;
    private final static float DEVIL_SPEED_INCREASE_PER_LEVEL = 1.5f;
    private final static HashMap<WeaponKind, Integer> weaponsLevelRequirementMap = new HashMap<>() {
        {
            put(WeaponKind.PISTOL, 0);
            put(WeaponKind.RIFLE, 5);
            put(WeaponKind.SHOTGUN, 10);
            put(WeaponKind.SNIPER, 15);
            put(WeaponKind.GRENADE_LAUNCHER, 20);
        }
    };
    
    private final static IntervalMap intervals = new IntervalMap();
    
    private enum Interval {
        SPAWN_ZOMBIE,
        SPAWN_DEVIL
    }
    
    private static void handleLevelingSystem() {
        if (Game.world.isGameOver()) return;
        int currentXp = Progress.PLAYER_CURRENT_XP.get();
        int maxXp = Progress.PLAYER_MAX_XP.get();
        if (currentXp < maxXp) return;
        
        Game.scene.getMessages().add("Level up!");
        
        // Level up
        Progress.PLAYER_CURRENT_LEVEL.set(Progress.PLAYER_CURRENT_LEVEL.get() + 1);
        Progress.PLAYER_CURRENT_XP.set(0);
        Progress.PLAYER_MAX_XP.set(maxXp + PLAYER_MAX_XP_INCREASE_PER_LEVEL);
        
        // Increase player hp
        Progress.PLAYER_CURRENT_HEALTH.set(
            Progress.PLAYER_CURRENT_HEALTH.get() + PLAYER_HEALTH_INCREASE_PER_LEVEL
        );
        Progress.PLAYER_MAX_HEALTH.set(
            Progress.PLAYER_MAX_HEALTH.get() + PLAYER_HEALTH_INCREASE_PER_LEVEL
        );
        
        // Increase zombie count
        if (Progress.ZOMBIE_COUNT.get() < Config.MAX_ZOMBIE_COUNT) {
            Progress.ZOMBIE_COUNT.set(
                Progress.ZOMBIE_COUNT.get() + ZOMBIE_COUNT_INCREASE_PER_LEVEL
            );
        }
        
        // Increase zombie damage
        Progress.ZOMBIE_DAMAGE.set(
            Progress.ZOMBIE_DAMAGE.get() + ZOMBIE_DAMAGE_INCREASE_PER_LEVEL
        );
        
        // Increase zombie speed
        if (Progress.ZOMBIE_SPEED.get() < Config.MAX_ZOMBIE_SPEED) {
            Progress.ZOMBIE_SPEED.set(
                Progress.ZOMBIE_SPEED.get() + ZOMBIE_SPEED_INCREASE_PER_LEVEL
            );
        }
        
        // Increase devil count
        if (
            Progress.DEVIL_COUNT.get() < Config.MAX_DEVIL_COUNT &&
                Progress.PLAYER_CURRENT_LEVEL.get() % 5 == 0
        ) {
            Progress.DEVIL_COUNT.set(
                Progress.DEVIL_COUNT.get() + DEVIL_COUNT_INCREASE_PER_5_LEVELS
            );
        }
        
        // Increase devil damage
        Progress.DEVIL_DAMAGE.set(
            Progress.DEVIL_DAMAGE.get() + DEVIL_DAMAGE_INCREASE_PER_LEVEL
        );
        
        // Increase devil speed
        if (Progress.DEVIL_SPEED.get() < Config.MAX_DEVIL_SPEED) {
            Progress.DEVIL_SPEED.set(
                Progress.DEVIL_SPEED.get() + DEVIL_SPEED_INCREASE_PER_LEVEL
            );
        }
        
        // Unlock weapons
        weaponsLevelRequirementMap.forEach((weaponKind, levelRequired) -> {
            if (
                !Progress.UNLOCKED_WEAPONS.contains(weaponKind) &&
                    Progress.PLAYER_CURRENT_LEVEL.get() >= levelRequired
            ) {
                Progress.UNLOCKED_WEAPONS.add(weaponKind);
                Game.scene.getMessages().add(
                    "You unlocked a " + weaponKind.name().toLowerCase() + "!"
                );
                
                Game.world.getPlayer().setCurrentWeapon(weaponKind);
            }
        });
        
        pickPowerUp();
    }
    
    private static void pickPowerUp() {
        Game.world.pause();
        
        Game.scene.setPowerUpSelectionComponentVisible(true);
        Game.scene.setOnSelectPowerUp(selectedPowerUp -> {
            selectedPowerUp.get().apply();
            Game.scene.getMessages().add("Your weapons has been upgraded!");
            Game.scene.setPowerUpSelectionComponentVisible(false);
            Game.world.play();
        });
    }
    
    private static void handleZombieSpawn() {
        if (Game.world.isGameOver()) return;
        
        if (!intervals.isIntervalOverFor(Interval.SPAWN_ZOMBIE)) return;
        intervals.resetIntervalFor(Interval.SPAWN_ZOMBIE);
        final int BATCH_SIZE = 10;
        int zombiesCount = Math.min(Progress.ZOMBIE_COUNT.get(), Config.MAX_ZOMBIE_COUNT);
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
    
    private static void handleDevilSpawn() {
        if (Game.world.isGameOver()) return;
        
        if (!intervals.isIntervalOverFor(Interval.SPAWN_DEVIL)) return;
        intervals.resetIntervalFor(Interval.SPAWN_DEVIL);
        int devilsCount = Math.min(Progress.DEVIL_COUNT.get(), Config.MAX_DEVIL_COUNT);
        if (Game.world.getDevils().size() < devilsCount) {
            // Spawn anywhere outside camera
            Vector randomPosition = Game.world.generateRandomPosition();
            while (Game.world.getCamera().isInViewport(randomPosition, 50)) {
                randomPosition.set(Game.world.generateRandomPosition());
            }
            
            Game.world.spawnDevil(randomPosition);
        }
    }
    
    private static void handleGameOver() {
        if (Game.world.isGameOver()) return;
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
        intervals.registerIntervalFor(Interval.SPAWN_DEVIL, 100);
    }
    
    private static boolean ticked = false;
    
    public static void update() {
        if (!ticked) {
            setup();
            ticked = true;
        }
        handleLevelingSystem();
        handleZombieSpawn();
        handleDevilSpawn();
        handleGameOver();
    }
}
