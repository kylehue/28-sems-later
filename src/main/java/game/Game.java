package game;

import event.KeyHandler;
import event.MouseHandler;
import game.utils.GameLoop;
import game.weapons.WeaponKind;
import javafx.concurrent.Task;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import main.GameApplication;
import scenes.GameScene;
import utils.Async;

public class Game extends GameLoop {
    public enum CollisionCategory {
        MAP,
        PLAYER,
        MOBS,
        PROJECTILES;
        
        private static class Accumulators {
            private static int lastBit = 1;
        }
        
        private final int bit;
        
        CollisionCategory() {
            this.bit = Accumulators.lastBit;
            Accumulators.lastBit <<= 1;
        }
        
        public int get() {
            return bit;
        }
    }
    
    public static class ZIndex {
        public static final int MAP_FLOOR = 10;
        public static final int PLAYER = 20;
        public static final int MOBS = 20;
        public static final int PROJECTILE = 21;
        public static final int MAP_DECORATIONS = 20;
        public static final int MAP_HIGH = 30;
    }
    
    public enum Control {
        SWITCH_TO_PISTOL(KeyCode.DIGIT1),
        SWITCH_TO_RIFLE(KeyCode.DIGIT2),
        SWITCH_TO_SHOTGUN(KeyCode.DIGIT3),
        SWITCH_TO_SNIPER(KeyCode.DIGIT4),
        SWITCH_TO_GRENADE_LAUNCHER(KeyCode.DIGIT5),
        SWITCH_TO_PREVIOUS_WEAPON(KeyCode.Q),
        MOVE_UP(KeyCode.W),
        MOVE_DOWN(KeyCode.S),
        MOVE_LEFT(KeyCode.A),
        MOVE_RIGHT(KeyCode.D),
        DASH(KeyCode.SPACE),
        SHOW_WEAPONS(KeyCode.F),
        PAUSE_GAME(KeyCode.ESCAPE);
        
        private final KeyCode weapon;
        
        Control(KeyCode weapon) {
            this.weapon = weapon;
        }
        
        public KeyCode get() {
            return weapon;
        }
    }
    
    public static final Canvas canvas = new Canvas();
    public static final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
    public static final KeyHandler keyHandler = new KeyHandler();
    public static final MouseHandler mouseHandler = new MouseHandler();
    public static World world;
    public static GameScene scene;
    
    public Game(GameScene scene) {
        Game.scene = scene;
        
        keyHandler.getKeyPressedProperty(Control.SHOW_WEAPONS).addListener(e -> {
            if (!keyHandler.isKeyPressed(Control.SHOW_WEAPONS)) return;
            scene.setWeaponSwitchComponentVisible(
                !scene.isWeaponSwitchComponentVisible()
            );
        });
        
        keyHandler.getKeyPressedProperty(Control.PAUSE_GAME).addListener(e -> {
            if (!keyHandler.isKeyPressed(Control.PAUSE_GAME)) return;
            
            boolean shouldPause = !scene.isPauseComponentVisible();
            
            if (shouldPause) {
                Game.world.pause();
                scene.setPauseComponentVisible(true);
            } else {
                Game.world.play();
                scene.setPauseComponentVisible(false);
            }
        });
        
        keyHandler.getKeyPressedProperty(Control.SWITCH_TO_PISTOL).addListener(e -> {
            if (!keyHandler.isKeyPressed(Control.SWITCH_TO_PISTOL)) return;
            if (world == null) return;
            if (world.isGameOver()) return;
            if (world.isPaused()) return;
            if (!Progress.UNLOCKED_WEAPONS.contains(WeaponKind.PISTOL)) {
                scene.getMessages().add("Pistol hasn't been unlocked yet!");
                return;
            }
            
            world.getPlayer().setCurrentWeapon(WeaponKind.PISTOL);
        });
        
        keyHandler.getKeyPressedProperty(Control.SWITCH_TO_RIFLE).addListener(e -> {
            if (!keyHandler.isKeyPressed(Control.SWITCH_TO_RIFLE)) return;
            if (world == null) return;
            if (world.isGameOver()) return;
            if (world.isPaused()) return;
            if (!Progress.UNLOCKED_WEAPONS.contains(WeaponKind.RIFLE)) {
                scene.getMessages().add("Rifle hasn't been unlocked yet!");
                return;
            }
            
            world.getPlayer().setCurrentWeapon(WeaponKind.RIFLE);
        });
        
        keyHandler.getKeyPressedProperty(Control.SWITCH_TO_SHOTGUN).addListener(e -> {
            if (!keyHandler.isKeyPressed(Control.SWITCH_TO_SHOTGUN)) return;
            if (world == null) return;
            if (world.isGameOver()) return;
            if (world.isPaused()) return;
            if (!Progress.UNLOCKED_WEAPONS.contains(WeaponKind.SHOTGUN)) {
                scene.getMessages().add("Shotgun hasn't been unlocked yet!");
                return;
            }
            
            world.getPlayer().setCurrentWeapon(WeaponKind.SHOTGUN);
        });
        
        keyHandler.getKeyPressedProperty(Control.SWITCH_TO_SNIPER).addListener(e -> {
            if (!keyHandler.isKeyPressed(Control.SWITCH_TO_SNIPER)) return;
            if (world == null) return;
            if (world.isGameOver()) return;
            if (world.isPaused()) return;
            if (!Progress.UNLOCKED_WEAPONS.contains(WeaponKind.SNIPER)) {
                scene.getMessages().add("Sniper hasn't been unlocked yet!");
                return;
            }
            
            world.getPlayer().setCurrentWeapon(WeaponKind.SNIPER);
        });
        
        keyHandler.getKeyPressedProperty(Control.SWITCH_TO_GRENADE_LAUNCHER).addListener(e -> {
            if (!keyHandler.isKeyPressed(Control.SWITCH_TO_GRENADE_LAUNCHER)) return;
            if (world == null) return;
            if (world.isGameOver()) return;
            if (world.isPaused()) return;
            if (!Progress.UNLOCKED_WEAPONS.contains(WeaponKind.GRENADE_LAUNCHER)) {
                scene.getMessages().add("Grenade Launcher hasn't been unlocked yet!");
                return;
            }
            
            world.getPlayer().setCurrentWeapon(WeaponKind.GRENADE_LAUNCHER);
        });
        
        keyHandler.getKeyPressedProperty(Control.SWITCH_TO_PREVIOUS_WEAPON).addListener(e -> {
            if (!keyHandler.isKeyPressed(Control.SWITCH_TO_PREVIOUS_WEAPON)) return;
            if (world == null) return;
            if (world.getPlayer().getPreviousWeapon() == null) return;
            if (world.isGameOver()) return;
            if (world.isPaused()) return;
            
            world.getPlayer().setCurrentWeapon(world.getPlayer().getPreviousWeapon());
        });
        
        
        scene.setOnContinueGame(() -> {
            Game.world.play();
            scene.setPauseComponentVisible(false);
        });
        
        scene.setOnExitGame(() -> {
            scene.getGameApplication().getSceneManager().setScene(
                GameApplication.Scene.TITLE
            );
            scene.setPauseComponentVisible(false);
            resetGame();
        });
    }
    
    public void initEventHandlers(Scene scene) {
        // Set up key handler & controls
        keyHandler.listen(scene);
        for (Control control : Control.values()) {
            keyHandler.registerKey(control, control.get());
        }
        
        // Set up mouse handler
        mouseHandler.listen(scene);
    }
    
    // private void preloadAssets()  {
    //     try{
    //         String resourcesDir = System.getProperty("user.dir") + "\\src\\main\\resources";
    //         Stream<Path> paths = Files.walk(Path.of(resourcesDir));
    //         for (Path path : paths.toList()) {
    //             if (!Files.isRegularFile(path)) continue;
    //             String pathStr = path.toString().replace(resourcesDir, "").replaceAll("\\\\", "/");
    //             if (pathStr.endsWith(".png")) {
    //                 Common.loadImage(pathStr);
    //             } else if (pathStr.endsWith(".mp3")) {
    //                 Common.loadMedia(pathStr);
    //             }
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }
    
    public void startGameSync() {
        if (world == null) {
            world = new World();
            world.start();
        }
        
        scene.setOtherGameComponentsVisible(true);
        scene.setPowerUpSelectionComponentVisible(false);
        scene.setWeaponSwitchComponentVisible(false);
        scene.setGameOverComponentVisible(false);
        scene.setPauseComponentVisible(false);
        
        if (!Config.IS_DEV_MODE) {
            Progress.reset();
        }
        
        startLoop();
    }
    
    public Task<Void> startGameAsync() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                startGameSync();
                return null;
            }
        };
        
        task.setOnFailed(e -> {
            System.out.println(e);
        });
        
        Async.queue2.submit(task);
        return task;
    }
    
    public void resetGame() {
        this.resetTimer();
        world.dispose();
        world = null;
    }
    
    private void clearCanvas() {
        graphicsContext.beginPath();
        graphicsContext.setFill(Paint.valueOf("#000000"));
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphicsContext.closePath();
    }
    
    private void renderFPS(GraphicsContext ctx) {
        ctx.beginPath();
        ctx.setFill(Paint.valueOf("#00FF00"));
        ctx.setFont(Font.font(null, FontWeight.BOLD, 24));
        ctx.setTextAlign(TextAlignment.RIGHT);
        ctx.setTextBaseline(VPos.BOTTOM);
        ctx.fillText(
            String.valueOf((int) getFPS()),
            ctx.getCanvas().getWidth() - 20,
            ctx.getCanvas().getHeight() - 20,
            100
        );
        ctx.closePath();
    }
    
    @Override
    public void render(float alpha) {
        graphicsContext.setImageSmoothing(false);
        
        clearCanvas();
        world.render(graphicsContext, alpha);
        
        if (Config.IS_DEV_MODE) {
            renderFPS(graphicsContext);
        }
    }
    
    @Override
    public void fixedUpdate(float deltaTime) {
        world.fixedUpdate(deltaTime);
    }
    
    @Override
    public void update(float deltaTime) {
        world.update(deltaTime);
    }
}
