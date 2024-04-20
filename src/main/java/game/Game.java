package game;

import event.KeyHandler;
import event.MouseHandler;
import game.ui.UI;
import game.utils.GameLoop;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Paint;
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
    public static KeyHandler keyHandler;
    public static MouseHandler mouseHandler;
    public static World world;
    public static UI ui;
    public static int FPS = 0;
    
    public Game() {
    
    }
    
    public void initEventHandlers(Scene scene) {
        // Set up key handler & controls
        keyHandler = new KeyHandler(scene);
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
        mouseHandler = new MouseHandler(scene);
    }
    
    public void startGameSync() {
        if (world == null) {
            world = new World();
            world.setup();
        }
        
        if (ui == null) {
            ui = new UI();
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
    
    public void pauseGame() {
        this.pauseLoop();
    }
    
    private void clearCanvas() {
        graphicsContext.beginPath();
        graphicsContext.setFill(Paint.valueOf("#000000"));
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphicsContext.closePath();
    }
    
    @Override
    public void render(float alpha) {
        graphicsContext.setImageSmoothing(false);
        
        clearCanvas();
        world.render(graphicsContext, alpha);
        ui.render(graphicsContext);
    }
    
    @Override
    public void fixedUpdate(float deltaTime) {
        world.fixedUpdate(deltaTime);
    }
    
    @Override
    public void update(float deltaTime) {
        world.update(deltaTime);
        FPS = (int) getFPS();
    }
}
