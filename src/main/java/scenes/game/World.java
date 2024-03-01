package scenes.game;

import entity.Enemy;
import entity.Player;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import main.GameApplication;
import utils.GameUtils;

import java.util.ArrayList;

public class World {
    private final Player player;
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    
    public World(GameApplication gameApplication) {
        this.player = new Player(gameApplication);
        
        Canvas canvas = gameApplication.getGameScene().getGraphicsContext().getCanvas();
        for (int i = 0; i < 50; i ++) {
            Enemy enemy = new Enemy(gameApplication);
            enemy.getPosition().setX(GameUtils.random(0, canvas.getWidth()));
            enemy.getPosition().setY(GameUtils.random(0, canvas.getHeight()));
            enemies.add(enemy);
        }
    }
    
    public void render(GraphicsContext ctx) {
        for (Enemy enemy : enemies) {
            enemy.render(ctx);
        }
        
        player.render(ctx);
    }
    
    public void update() {
        player.update();
        
        for (Enemy enemy : enemies) {
            enemy.update();
        }
    }
    
    public Player getPlayer() {
        return player;
    }
}
