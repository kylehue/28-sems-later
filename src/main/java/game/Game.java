package game;

import event.KeyHandler;
import event.MouseHandler;
import game.utils.GameLoop;
import game.weapons.Weapon;
import javafx.concurrent.Task;
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
    public enum CollisionGroup {
        MAP,
        PLAYER,
        MOBS,
        PROJECTILES,
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
        
        Progress.reset();
        
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
        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.fillText(
            String.valueOf(getFPS()), ctx.getCanvas().getWidth() - 30,
            30,
            100
        );
        ctx.closePath();
    }
    
    @Override
    public void render(float alpha) {
        graphicsContext.setImageSmoothing(false);
        
        clearCanvas();
        world.render(graphicsContext, alpha);
        
        // renderFPS(graphicsContext);
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
