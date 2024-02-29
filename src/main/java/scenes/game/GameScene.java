package scenes.game;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import main.GameApplication;
import scenes.GameApplicationScene;
import utils.GameLoop;
import utils.KeyHandler;

public class GameScene extends GameApplicationScene {
    private final Canvas canvas = new Canvas();
    private final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
    private final GameLoop gameLoop;
    private final KeyHandler keyHandler;
    private World world;
    
    public GameScene(GameApplication gameApplication, String sceneId) {
        super(gameApplication, sceneId);
        
        // Set up game loop
        GameScene $this = this;
        this.gameLoop = new GameLoop() {
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
    }
    
    public KeyHandler getKeyHandler() {
        return keyHandler;
    }
    
    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }
    
    public void startGame() {
        if (this.world == null) {
            this.world = new World(this.getGameApplication());
        }
        
        this.gameLoop.startLoop();
    }
    
    public void pauseGame() {
        this.gameLoop.pauseLoop();
    }
    
    public void render() {
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        world.render(graphicsContext);
    }
    
    public void update(double deltaTime) {
        world.update();
    }
}
