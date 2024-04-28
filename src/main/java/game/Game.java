package game;

import event.KeyHandler;
import event.MouseHandler;
import game.utils.GameLoop;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
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
        public static final int ZOMBIE = 20;
        public static final int MAP_DECORATIONS = 20;
        public static final int MAP_HIGH = 30;
    }
    
    public static final Canvas canvas = new Canvas();
    public static final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
    public static final KeyHandler keyHandler = new KeyHandler();
    public static final MouseHandler mouseHandler = new MouseHandler();
    public static World world;
    public static GameScene scene;
    
    public Game(GameScene scene) {
        Game.scene = scene;
        
        keyHandler.getKeyPressedProperty("weapon-switch").addListener(e -> {
            if (!keyHandler.isKeyPressed("weapon-switch")) return;
            scene.setWeaponSwitchComponentVisible(
                !scene.isWeaponSwitchComponentVisible()
            );
        });
    }
    
    public void initEventHandlers(Scene scene) {
        // Set up key handler & controls
        keyHandler.listen(scene);
        keyHandler.registerKey("weapon-switch", KeyCode.F);
        keyHandler.registerKey("up", KeyCode.UP);
        keyHandler.registerKey("up", KeyCode.W);
        keyHandler.registerKey("down", KeyCode.DOWN);
        keyHandler.registerKey("down", KeyCode.S);
        keyHandler.registerKey("left", KeyCode.LEFT);
        keyHandler.registerKey("left", KeyCode.A);
        keyHandler.registerKey("right", KeyCode.RIGHT);
        keyHandler.registerKey("right", KeyCode.D);
        keyHandler.registerKey("dash", KeyCode.SPACE);
        
        // Set up mouse handler
        mouseHandler.listen(scene);
    }
    
    public void startGameSync() {
        if (world == null) {
            world = new World();
            world.start();
        }
        
        scene.setOtherGameComponentsVisible(true);
        scene.setPowerUpSelectionComponentVisible(false);
        scene.setWeaponSwitchComponentVisible(false);
        scene.setGameOverComponentVisible(false);
        
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
    
    public void pauseGame() {
        this.pauseLoop();
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
