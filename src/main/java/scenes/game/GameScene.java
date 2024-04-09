package scenes.game;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Paint;
import main.GameApplication;
import map.Map;
import scenes.GameApplicationScene;
import event.KeyHandler;
import event.MouseHandler;
import utils.AnimationLoop;
import utils.Async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameScene extends GameApplicationScene {
    private final Canvas canvas = new Canvas();
    private final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
    private final AnimationLoop animationLoop;
    private final KeyHandler keyHandler;
    private final MouseHandler mouseHandler;
    private World world;
    
    public GameScene(GameApplication gameApplication, String sceneId) {
        super(gameApplication, sceneId);
        
        // Set up game loop
        GameScene $this = this;
        this.animationLoop = new AnimationLoop() {
            @Override
            public void render() {
                $this.render();
            }
            
            @Override
            public void fixedUpdate(float deltaTime) {
                $this.fixedUpdate(deltaTime);
            }
        };
        
        // Add canvas to game scene
        this.getDefaultRoot().getChildren().add(canvas);
        canvas.widthProperty().bind(this.getScene().widthProperty());
        canvas.heightProperty().bind(this.getScene().heightProperty());
        
        // Set up key handler & controls
        this.keyHandler = new KeyHandler(this.getScene());
        this.keyHandler.registerKey("up", KeyCode.UP);
        this.keyHandler.registerKey("up", KeyCode.W);
        this.keyHandler.registerKey("down", KeyCode.DOWN);
        this.keyHandler.registerKey("down", KeyCode.S);
        this.keyHandler.registerKey("left", KeyCode.LEFT);
        this.keyHandler.registerKey("left", KeyCode.A);
        this.keyHandler.registerKey("right", KeyCode.RIGHT);
        this.keyHandler.registerKey("right", KeyCode.D);
        this.keyHandler.registerKey("dash", KeyCode.SPACE);
        
        // Set up mouse handler
        this.mouseHandler = new MouseHandler(this.getScene());
    }
    
    public AnimationLoop getGameLoop() {
        return animationLoop;
    }
    
    public KeyHandler getKeyHandler() {
        return keyHandler;
    }
    
    public MouseHandler getMouseHandler() {
        return mouseHandler;
    }
    
    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }
    
    public World getWorld() {
        return world;
    }
    
    public void startGame() {
        Async.executorService.submit(() -> {
            if (this.world == null) {
                this.world = new World(this.getGameApplication());
                this.world.setup();
            }
            
            this.animationLoop.startLoop();
        });
    }
    
    public void pauseGame() {
        this.animationLoop.pauseLoop();
    }
    
    public void render() {
        graphicsContext.beginPath();
        graphicsContext.setFill(Paint.valueOf("#000000"));
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphicsContext.closePath();
        world.render(graphicsContext);
        graphicsContext.setImageSmoothing(false);
    }
    
    public void fixedUpdate(float deltaTime) {
        world.update(deltaTime);
    }
}
