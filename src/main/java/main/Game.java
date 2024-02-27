package main;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import utils.GameLoop;

public class Game extends GameLoop {
    private final Canvas canvas = new Canvas();
    private final GraphicsContext ctx = canvas.getGraphicsContext2D();
    private final World world = new World();
    public Game() {
    
    }
    
    public Canvas getCanvas() {
        return canvas;
    }
    
    public void startGameLoop() {
        this.startLoop();
    }
    
    @Override
    public void render() {
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        world.render(ctx);
    }
    
    @Override
    public void update(double deltaTime) {
        world.update();
    }
}
