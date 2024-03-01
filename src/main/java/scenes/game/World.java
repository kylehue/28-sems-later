package scenes.game;

import entity.Enemy;
import entity.Player;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import main.GameApplication;
import utils.Camera;
import utils.GameUtils;

import java.util.ArrayList;

public class World {
    private final Player player;
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final Camera camera;
    
    public World(GameApplication gameApplication) {
        this.camera = new Camera(gameApplication.getGameScene().getGraphicsContext());
        this.player = new Player(gameApplication);
        
        Canvas canvas = gameApplication.getGameScene().getGraphicsContext().getCanvas();
        for (int i = 0; i < 50; i++) {
            Enemy enemy = new Enemy(gameApplication);
            enemy.getPosition().setX(GameUtils.random(0, canvas.getWidth()));
            enemy.getPosition().setY(GameUtils.random(0, canvas.getHeight()));
            enemies.add(enemy);
        }
    }
    
    public void render(GraphicsContext ctx) {
        this.camera.begin();
        
        for (Enemy enemy : enemies) {
            enemy.render(ctx);
        }
        
        player.render(ctx);
        
        this.camera.end();
    }
    
    public void update() {
        player.update();
        
        for (Enemy enemy : enemies) {
            // enemy.update();
        }
        
        this.camera.moveTo(player.getPosition());
        this.camera.zoomTo(700);
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public Camera getCamera() {
        return camera;
    }
}
