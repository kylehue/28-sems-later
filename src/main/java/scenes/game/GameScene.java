package scenes.game;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import main.GameApplication;
import map.Map;
import scenes.GameApplicationScene;
import event.KeyHandler;
import event.MouseHandler;
import utils.AnimationLoop;

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
            public void update(double deltaTime) {
                $this.update(deltaTime);
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
        if (this.world == null) {
            // TODO: how can we assign the world to this.world BEFORE creating it?
            this.world = new World(this.getGameApplication());
            this.world.setup();
        }
        
        this.animationLoop.startLoop();
    }
    
    public void pauseGame() {
        this.animationLoop.pauseLoop();
    }
    
    public void render() {
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        world.render(graphicsContext);
        graphicsContext.setImageSmoothing(false);
    }
    
    public void update(double deltaTime) {
        world.update(deltaTime);
    }
}
